package controller

import integration.BaseIntegrationSpec
import play.api.test._
import play.api.test.Helpers._
import org.scalatestplus.play._
import play.api.Play
import dal.WorkshopRepository
import models.Workshop
import java.time.Instant
import java.time.temporal.TemporalUnit
import java.time.temporal.ChronoUnit.DAYS

class RegistrationControllerSpec extends BaseIntegrationSpec {

  "Index" must {
    "show that no workshop is active" in {
      val Some(home) = route(FakeRequest(GET, "/"))
      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include("Zur Zeit ist kein Workshop geplant.")
    }

    "show the active workshop" in {
      var repo = Play.current.injector.instanceOf(classOf[WorkshopRepository])
      var createFuture = repo.insert(new Workshop(Option.empty,
        Instant.now().minus(1L, DAYS).toString(),
        Instant.now().plus(1L, DAYS).toString(),
        15, "Sonntag, 17. April 13:00 - 18:00", "JavaScript", "bla"))
      createFuture.futureValue mustBe 1

      val Some(home) = route(FakeRequest(GET, "/"))
      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include("JavaScript")
      contentAsString(home) must include("bla")
      contentAsString(home) must include("Sonntag, 17. April 13:00 - 18:00")
    }

  }
}
