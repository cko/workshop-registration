import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.test._
import play.api.test.Helpers._

import org.scalatestplus.play._
import integration.BaseIntegrationBrowserSpec


class BrowserSpec extends BaseIntegrationBrowserSpec {

  "Application" must {

    "show no workshop if no workshop is active" in {
      go to "http://localhost:" + port
      pageSource must include ("Zur Zeit ist kein Workshop geplant.")
    }

    "show registration page if workshop is active" in {
      createWorkshop()
      go to "http://localhost:" + port
      pageSource must include ("Einführung in JavaScript")
    }

    "register new participant if workshop is active" in {
      createWorkshop()
      go to "http://localhost:" + port
      pageSource must include ("Anmeldung")

      textField("email").value = "test@example.com"
      textField("name").value = "John Doe"
      submit()
      pageSource must include ("Anmeldung wurde gespeichert")
      pageSource must include ("John Doe")
    }

    "display participant list for admin" in  {
      go to ("http://localhost:" + port + "/admin/registrations")
      pageSource must include ("Authentication required")
    }
  }
}
