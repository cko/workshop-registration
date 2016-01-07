package dal

import scala.annotation.implicitNotFound
import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits
import scala.concurrent.Future

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.impl.daos.DelegableAuthInfoDAO

import javax.inject.Inject
import javax.inject.Singleton
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfig
import slick.driver.H2Driver.api._
import slick.driver.JdbcProfile

@Singleton
class PasswordInfoRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) extends DelegableAuthInfoDAO[PasswordInfo] with DBTableDefinitions with HasDatabaseConfig[JdbcProfile] {
  
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  override def find(loginInfo: LoginInfo): Future[Option[PasswordInfo]] = {
    val pwQuery = for {
      dbLoginInfo <- loginInfoQuery(loginInfo)
      dbPasswordInfo <- slickPasswordInfos.filter(_.loginInfoId === dbLoginInfo.id)
    } yield dbPasswordInfo
    db.run(pwQuery.result.headOption).map { dbPasswordInfoOption =>
      dbPasswordInfoOption.map { pwInfo =>
        PasswordInfo(pwInfo.hasher, pwInfo.password, pwInfo.salt)
      }
    }
  }

  def getLoginInfoId(loginInfo: LoginInfo): Future[Long] = db.run {
    slickLoginInfos.filter(info => info.providerID === loginInfo.providerID && info.providerKey === loginInfo.providerKey).map(_.id)
      .result.headOption
  }.map(_.getOrElse(throw new Exception("Associated LoginInfo not found")))

  override def update(loginInfo: LoginInfo, passwordInfo: PasswordInfo): Future[PasswordInfo] =
    getLoginInfoId(loginInfo).flatMap {
      loginInfoId => db.run(slickPasswordInfos.update(DBPasswordInfo(passwordInfo.hasher, passwordInfo.password, passwordInfo.salt, loginInfoId)))
    }.map(_ => passwordInfo)

  override def remove(loginInfo: LoginInfo): Future[Unit] =
    getLoginInfoId(loginInfo).flatMap {
      loginInfoId => db.run(slickPasswordInfos.filter(_.loginInfoId === loginInfoId).delete)
    }.map(_ => ())

  override def save(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] =
    find(loginInfo).flatMap {
      case Some(_) => update(loginInfo, authInfo)
      case None    => add(loginInfo, authInfo)
    }

  override def add(loginInfo: LoginInfo, passwordInfo: PasswordInfo): Future[PasswordInfo] =
    getLoginInfoId(loginInfo).flatMap {
      loginInfoId => db.run(slickPasswordInfos += DBPasswordInfo(passwordInfo.hasher, passwordInfo.password, passwordInfo.salt, loginInfoId))
    }.map(_ => passwordInfo)

  def passwordInfo2db(loginInfoId: Long, pwInfo: PasswordInfo) = DBPasswordInfo(pwInfo.hasher, pwInfo.password, pwInfo.salt, loginInfoId)
  def db2PasswordInfo(pwInfo: DBPasswordInfo): PasswordInfo = new PasswordInfo(pwInfo.hasher, pwInfo.password, pwInfo.salt)

}
