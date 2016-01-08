package dal

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.Play
import models.User
import java.util.UUID


class PasswordInfoRepositorySpec extends BaseRepositorySpec {

  "PasswordInfoRepository" must {
    "store passwordInfo" in {
      
      whenReady(storeUser) { user =>
      
      var repo = Play.current.injector.instanceOf(classOf[PasswordInfoRepository])
      var passwordInfo =  PasswordInfo("", "plainPassword")
      var futureCreate = repo.save(user.loginInfo, passwordInfo)

      whenReady(futureCreate) { resultPasswordInfo =>
        resultPasswordInfo.password mustBe "plainPassword"
        resultPasswordInfo.hasher mustBe ""
        var futurePasswordInfo = repo.find(user.loginInfo)

        whenReady(futurePasswordInfo){ resultPasswordInfo =>
          resultPasswordInfo.get.password mustBe "plainPassword"
          resultPasswordInfo.get.hasher mustBe ""
        }
      }
      }
    }
  }
  
  def storeUser() = {
    val email = "test@example.com"
    val firstname = "firstName"
    var repo = Play.current.injector.instanceOf(classOf[UserRepository])
    val loginInfo = LoginInfo(CredentialsProvider.ID, "test@example.com")
    val uuid = UUID.randomUUID()
    val user = User(
            userID = uuid,
            loginInfo = loginInfo,
            firstName = Some(firstname),
            lastName = Some("lastName"),
            email = Some(email)
    )
    repo.save(user)
  }
}
