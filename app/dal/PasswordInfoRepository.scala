package dal

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.impl.daos.DelegableAuthInfoDAO
import dal.PasswordInfoRepository._
import play.api.libs.concurrent.Execution.Implicits._

import scala.collection.mutable
import scala.concurrent.Future

class PasswordInfoRepository extends DelegableAuthInfoDAO[PasswordInfo] {


  def find(loginInfo: LoginInfo): Future[Option[PasswordInfo]] = {
    Future.successful(data.get(loginInfo))
  }

  def add(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = {
    data += (loginInfo -> authInfo)
    Future.successful(authInfo)
  }

  def update(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = {
    data += (loginInfo -> authInfo)
    Future.successful(authInfo)
  }

  def save(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = {
    find(loginInfo).flatMap {
      case Some(_) => update(loginInfo, authInfo)
      case None => add(loginInfo, authInfo)
    }
  }

  def remove(loginInfo: LoginInfo): Future[Unit] = {
    data -= loginInfo
    Future.successful(())
  }
}

object PasswordInfoRepository {

  var data: mutable.HashMap[LoginInfo, PasswordInfo] = mutable.HashMap()
}