package dal

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.Play


class PasswordInfoRepositorySpec extends PlaySpec with ScalaFutures with OneAppPerSuite {

  "PasswordInfoRepository" must {
    "store passwordInfo" in {
      var repo = Play.current.injector.instanceOf(classOf[PasswordInfoRepository])
      var passwordInfo =  PasswordInfo("", "plainPassword")
      val loginInfo = LoginInfo(CredentialsProvider.ID, "test@example.com")
      var futureCreate = repo.save(loginInfo, passwordInfo)

      whenReady(futureCreate) { resultPasswordInfo =>
        resultPasswordInfo.password mustBe "plainPassword"
        resultPasswordInfo.hasher mustBe ""
        var futurePasswordInfo = repo.find(loginInfo)

        whenReady(futurePasswordInfo){ resultPasswordInfo =>
          resultPasswordInfo.get.password mustBe "plainPassword"
          resultPasswordInfo.get.hasher mustBe ""
        }
      }
    }
  }

}
