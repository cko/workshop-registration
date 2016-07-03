package integration

import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatest.BeforeAndAfter
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.db.DBApi
import play.api.db.evolutions.Evolutions
import play.api.inject.guice.GuiceApplicationBuilder

trait BaseIntegrationAppSpec extends BaseIntegrationSpec with OneAppPerSuite  {

}