package dal

import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play.OneAppPerSuite
import org.scalatestplus.play.PlaySpec

import play.api.Play


class RegistrationRepositorySpec extends PlaySpec with ScalaFutures with OneAppPerSuite {

  "RegistrationRepository" must {
    "store registrations" in {
      var repo = Play.current.injector.instanceOf(classOf[RegistrationRepository])
      var futureCreate = repo.create("John Doe", "test@example.com")

      whenReady(futureCreate) { result =>
        result.email mustBe("test@example.com")
        result.name mustBe ("John Doe")
        var futureList = repo.list()

        whenReady(futureList){ result =>
          result.length mustBe (1)
        }
      }
    }
  }
}
