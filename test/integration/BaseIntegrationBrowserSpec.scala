package integration

import org.scalatestplus.play.FirefoxFactory
import org.scalatestplus.play.OneBrowserPerSuite
import org.scalatestplus.play.OneServerPerSuite


trait BaseIntegrationBrowserSpec extends BaseIntegrationSpec with OneBrowserPerSuite with OneServerPerSuite with FirefoxFactory {

}