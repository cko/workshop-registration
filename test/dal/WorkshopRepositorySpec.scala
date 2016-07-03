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
      var futureCreate = repo.insert(new Workshop(Option.empty,
        Instant.now().minus(1L, DAYS).toString(),
        Instant.now().plus(1L, DAYS).toString(), 
        15, "Sonntag, 17. April 13:00 - 18:00", "JavaScript", "bla"))

      whenReady(futureCreate) { result1 =>
        result1 mustBe (1)
        var futureList = repo.list()

        whenReady(futureList) { result2 =>
          result2.length mustBe (1)
          var futureActive = repo.active()

          whenReady(futureActive) { result3 =>
            result3.title == "JavaScript"
          }
        }
      }
    }
  }
}
