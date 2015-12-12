import dal.RegistrationRepository
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.Play
import org.scalatest.concurrent.ScalaFutures


class RegistrationRepositorySpec extends PlaySpec with ScalaFutures with OneAppPerSuite {//with Matchers {

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
