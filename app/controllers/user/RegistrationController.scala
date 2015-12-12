package controllers.user

import javax.inject.Inject

import dal.RegistrationRepository
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

class RegistrationController @Inject() (repo: RegistrationRepository, val messagesApi: MessagesApi)
                                       (implicit ec: ExecutionContext) extends Controller with I18nSupport {

  /**
   * The mapping for the person form.
   */
  val registrationForm: Form[CreateRegistrationForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "email" -> email
    )(CreateRegistrationForm.apply)(CreateRegistrationForm.unapply)
  }

  def index = Action {
    Ok(views.html.index(registrationForm))
  }


  def addRegistration = Action.async { implicit request =>
      registrationForm.bindFromRequest().fold(
        formWithErrors => Future.successful(Ok(views.html.index(formWithErrors))), //BadRequest(views.html.index(formWithErrors)),
        registration => {
            repo.create(registration.name, registration.email).map { _ =>
              Ok(views.html.registration_successful(registration.name))
          }
        }
      )
  }
}

/**
 * The create person form.
 *
 * Generally for forms, you should define separate objects to your models, since forms very often need to present data
 * in a different way to your models.  In this case, it doesn't make sense to have an id parameter in the form, since
 * that is generated once it's created.
 */
case class CreateRegistrationForm(name: String, email: String)
