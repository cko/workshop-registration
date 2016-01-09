package dal

import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.OneAppPerSuite
import org.scalatest.BeforeAndAfter
import play.api.db.evolutions.Evolutions
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.db.DBApi

trait BaseRepositorySpec extends PlaySpec with ScalaFutures with OneAppPerSuite with BeforeAndAfter {
  
   lazy val appBuilder = new GuiceApplicationBuilder()

  lazy val injector = appBuilder.injector()

  lazy val databaseApi = injector.instanceOf[DBApi]
  
  before {
      Evolutions.applyEvolutions(databaseApi.database("default"))
  }
    
  after {
      Evolutions.cleanupEvolutions(databaseApi.database("default"))
  }
  
}