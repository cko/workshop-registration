package dal

import integration.BaseIntegrationSpec
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play.OneAppPerSuite
import org.scalatestplus.play.PlaySpec

import play.api.Play
import models.Workshop
import java.sql.Timestamp
import java.time.Instant
import java.time.temporal.TemporalUnit
import java.time.temporal.ChronoUnit.DAYS

class WorkshopRepositorySpec extends BaseIntegrationSpec {

  "WorkshopRepository" must {
    "store workshops" in {
      var repo = Play.current.injector.instanceOf(classOf[WorkshopRepository])

      var createFuture = repo.insert(new Workshop(Option.empty,
        Instant.now().minus(1L, DAYS).toString(),
        Instant.now().plus(1L, DAYS).toString(),
        15, "Sonntag, 17. April 13:00 - 18:00", "JavaScript", "bla"))
      createFuture.futureValue mustBe 1

      var listFuture = repo.list()
      listFuture.futureValue.size mustBe 1

      var countFuture = repo.count()
      countFuture.futureValue mustBe 1

      var activeFuture = repo.active()
      activeFuture.futureValue.get.title == "JavaScript"
    }

    "return empty option if no active workshop" in {
      var repo = Play.current.injector.instanceOf(classOf[WorkshopRepository])
      repo.active().futureValue mustBe empty
    }

    "return empty list if no active workshop" in {
      var repo = Play.current.injector.instanceOf(classOf[WorkshopRepository])
      repo.list().futureValue mustBe empty
    }
  }
}
