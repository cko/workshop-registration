package controllers.user

import javax.inject.Inject

import dal.RegistrationRepository
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import play.api.Play

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
    //TODO write tests, add dates
    val maxNumber = Play.current.configuration.getInt("workshop.maxnumber").get
    val allRegistrations = repo.list().value
    var currentNumber = 0
    if (!allRegistrations.isEmpty){
      currentNumber = allRegistrations.get.get.size
    } 
    
    //val registrationStart = Play.current.configuration.getString("registration.start")
    //val registrationEnd = Play.current.configuration.getString("registration.end")
    val registrationEnabled = currentNumber < maxNumber
    Ok(views.html.index(registrationForm, registrationEnabled))
  }


  def addRegistration = Action.async { implicit request =>
      registrationForm.bindFromRequest().fold(
        formWithErrors => Future.successful(Ok(views.html.index(formWithErrors, true))), //BadRequest(views.html.index(formWithErrors)),
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
