package dal

import integration.BaseIntegrationAppSpec
import play.api.Play


class RegistrationRepositorySpec extends BaseIntegrationAppSpec {

  "RegistrationRepository" must {
    "store registrations" in {
      var repo = Play.current.injector.instanceOf(classOf[RegistrationRepository])
      var futureCreate = repo.create("John Doe", "test@example.com", 1)

      whenReady(futureCreate) { result =>
        result.email mustBe("test@example.com")
        result.name mustBe ("John Doe")
        var futureList = repo.list(1)

        whenReady(futureList){ result =>
          result.length mustBe (1)
        }
      }
    }
  }
}
