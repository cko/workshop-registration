package service

import java.util.UUID
import javax.inject.Inject
import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.impl.providers.CommonSocialProfile
import play.api.libs.concurrent.Execution.Implicits._
import scala.concurrent.Future
import com.mohiva.play.silhouette.api.services.IdentityService
import dal.UserRepository
import models.User


class UserService @Inject() (userDAO: UserRepository) extends IdentityService[User] {

  def retrieve(loginInfo: LoginInfo): Future[Option[User]] = userDAO.find(loginInfo)


  def save(user: User) = userDAO.save(user)

}
