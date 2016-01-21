import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.test._
import play.api.test.Helpers._

@RunWith(classOf[JUnitRunner])
class BrowserSpec extends Specification {

  "Application" should {

    "works from within a browser" in new WithBrowser(webDriver = WebDriverFactory(FIREFOX)) {
      browser.goTo("http://localhost:" + port)
      browser.pageSource must contain("JavaScript Workshop")
    }

    "register new participant" in new WithBrowser(webDriver = WebDriverFactory(FIREFOX)) {
      browser.goTo("http://localhost:" + port)
      browser.pageSource must contain("Anmeldung")
      browser.fill("#email").`with`("test@example.com")
      browser.fill("#name").`with`("John Doe")
      browser.submit("#submit")
      browser.pageSource must contain("John Doe")
    }

    "display participant list for admin" in new WithBrowser(webDriver = WebDriverFactory(FIREFOX)) {
      browser.goTo("http://localhost:" + port + "/admin/registrations")
      browser.pageSource must contain("Authentication required")
    }
  }
}
