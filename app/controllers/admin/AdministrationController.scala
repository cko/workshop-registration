package controllers.admin

import javax.inject.Inject
import dal.RegistrationRepository
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, Controller}
import scala.concurrent.ExecutionContext
import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import models.User
import com.mohiva.play.silhouette.api.Environment

class AdministrationController @Inject() (repo: RegistrationRepository, val messagesApi: MessagesApi, val env: Environment[User, CookieAuthenticator])
                                          (implicit ec: ExecutionContext) extends Silhouette[User, CookieAuthenticator] with I18nSupport {

  def getRegistrations = SecuredAction.async {
    repo.list().map { registrations =>
      Ok(views.html.registered(registrations))
    }
  }
}

