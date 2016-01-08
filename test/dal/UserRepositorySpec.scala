  package dal

import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.OneAppPerSuite
import play.api.Play
import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import models.User
import java.util.UUID

class UserRepositorySpec extends BaseRepositorySpec {
  
    "UserRepository" must {
    "store users" in {
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
      var futureCreate = repo.save(user)

      whenReady(futureCreate) { result =>
        result.email.get mustBe email
        result.firstName.get mustBe firstname
        var futureUser = repo.find(uuid)

        whenReady(futureUser){ user =>
          result.email.get mustBe email
          result.firstName.get mustBe firstname
        }
      }
    }
  }
  
}