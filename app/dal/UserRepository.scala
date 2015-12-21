package dal

import java.util.UUID
import com.mohiva.play.silhouette.api.LoginInfo
import models.User
import scala.collection.mutable
import scala.concurrent.Future
import play.api.db.slick.DatabaseConfigProvider
import javax.inject.{ Inject, Singleton }
import scala.concurrent.ExecutionContext


@Singleton
class UserRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  
  val users: mutable.HashMap[UUID, User] = mutable.HashMap()
  
  def find(loginInfo: LoginInfo) = Future.successful(
    users.find { case (id, user) => user.loginInfo == loginInfo }.map(_._2)
  )

  def find(userID: UUID) = Future.successful(users.get(userID))

  def save(user: User) = {
    users += (user.userID -> user)
    Future.successful(user)
  }
}
