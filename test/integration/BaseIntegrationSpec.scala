package integration

import java.time.Instant
import java.time.temporal.ChronoUnit.DAYS

import org.scalatest.BeforeAndAfter
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.Millis
import org.scalatest.time.Seconds
import org.scalatest.time.Span
import org.scalatestplus.play.PlaySpec

import dal.WorkshopRepository
import javax.inject.Singleton
import models.Workshop
import play.api.Play
import play.api.db.DBApi
import play.api.db.evolutions.Evolutions
import play.api.inject.guice.GuiceApplicationBuilder

trait BaseIntegrationSpec extends PlaySpec with ScalaFutures with BeforeAndAfter {

  implicit val defaultPatience =
    PatienceConfig(timeout = Span(5, Seconds), interval = Span(10, Millis))

  lazy val appBuilder = new GuiceApplicationBuilder()

  lazy val injector = appBuilder.injector()

  lazy val databaseApi = injector.instanceOf[DBApi]
  
  before {
      // cleanup before to make sure DB is clean from BrowserSpec run
      Evolutions.cleanupEvolutions(databaseApi.database("default"))
      Evolutions.applyEvolutions(databaseApi.database("default"))
  }
    
  after {
      Evolutions.cleanupEvolutions(databaseApi.database("default"))
  }

  def createWorkshop() {
    var repo = Play.current.injector.instanceOf(classOf[WorkshopRepository])
    var createFuture = repo.insert(new Workshop(Option.empty,
      Instant.now().minus(1L, DAYS).toString(),
      Instant.now().plus(1L, DAYS).toString(),
      15, "Sonntag, 17. April 13:00 - 18:00", "Einführung in JavaScript", "bla"))
    createFuture.futureValue mustBe 1
  }
}