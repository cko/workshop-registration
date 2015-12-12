package controllers.admin

import javax.inject.Inject

import dal.RegistrationRepository
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext

class AdministrationController @Inject() (repo: RegistrationRepository, val messagesApi: MessagesApi)
                                          (implicit ec: ExecutionContext) extends Controller with I18nSupport {

  def getRegistrations = Action.async {
    repo.list().map { registrations =>
      Ok(views.html.registered(registrations))
    }
  }
}

