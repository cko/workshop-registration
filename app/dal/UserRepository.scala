package dal

import java.util.UUID
import com.mohiva.play.silhouette.api.LoginInfo
import models.User
import slick.driver.JdbcProfile
import play.api.db.slick.{HasDatabaseConfigProvider, DatabaseConfigProvider}
import javax.inject.{ Inject, Singleton }
import scala.concurrent.ExecutionContext
import play.api.db.slick.HasDatabaseConfigProvider


@Singleton
class UserRepository @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) extends DBTableDefinitions with HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  def find(loginInfo: LoginInfo) = {
    val userQuery = for {
      dbLoginInfo <- loginInfoQuery(loginInfo)
      dbUserLoginInfo <- slickUserLoginInfos.filter(_.loginInfoId === dbLoginInfo.id)
      dbUser <- slickUsers.filter(_.id === dbUserLoginInfo.userID)
    } yield dbUser
    db.run(userQuery.result.headOption).map { dbUserOption =>
      dbUserOption.map { user =>
        User(UUID.fromString(user.userID), loginInfo, user.firstName, user.lastName, user.email)
      }
    }
  }

  def find(userID: UUID) = {
    val query = for {
      dbUser <- slickUsers.filter(_.id === userID.toString)
      dbUserLoginInfo <- slickUserLoginInfos.filter(_.userID === dbUser.id)
      dbLoginInfo <- slickLoginInfos.filter(_.id === dbUserLoginInfo.loginInfoId)
    } yield (dbUser, dbLoginInfo)
    db.run(query.result.headOption).map { resultOption =>
      resultOption.map {
        case (user, loginInfo) =>
          User(
            UUID.fromString(user.userID),
            LoginInfo(loginInfo.providerID, loginInfo.providerKey),
            user.firstName,
            user.lastName,
            user.email)
      }
    }
  }

  def save(user: User) = {
    val dbUser = DBUser(user.userID.toString, user.firstName, user.lastName, user.email)
    val dbLoginInfo = DBLoginInfo(None, user.loginInfo.providerID, user.loginInfo.providerKey)
    // We don't have the LoginInfo id so we try to get it first.
    // If there is no LoginInfo yet for this user we retrieve the id on insertion.
    val loginInfoAction = {
      val retrieveLoginInfo = slickLoginInfos.filter(
        info => info.providerID === user.loginInfo.providerID &&
          info.providerKey === user.loginInfo.providerKey).result.headOption
      val insertLoginInfo = slickLoginInfos.returning(slickLoginInfos.map(_.id)).
        into((info, id) => info.copy(id = Some(id))) += dbLoginInfo
      for {
        loginInfoOption <- retrieveLoginInfo
        loginInfo <- loginInfoOption.map(DBIO.successful(_)).getOrElse(insertLoginInfo)
      } yield loginInfo
    }
    // combine database actions to be run sequentially
    val actions = (for {
      _ <- slickUsers.insertOrUpdate(dbUser)
      loginInfo <- loginInfoAction
      _ <- slickUserLoginInfos += DBUserLoginInfo(dbUser.userID, loginInfo.id.get)
    } yield ()).transactionally
    db.run(actions).map(_ => user)
  }
}
