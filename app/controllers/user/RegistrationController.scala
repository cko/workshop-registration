package controllers.user

import java.text.SimpleDateFormat
import java.util.{Calendar, Date}
import javax.inject.Inject

import dal.{UserRepository, RegistrationRepository}
import org.apache.commons.lang3.time.DateUtils
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import play.api.Play
import service.UserService

import scala.concurrent.{ExecutionContext, Future}

class RegistrationController @Inject() (val registrationRepo: RegistrationRepository, val userService: UserService, val messagesApi: MessagesApi)
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

  def index = Action.async {
    //TODO write tests, add dates
    var notBookedOut = userService.isNotBookedOut
    var notOver = userService.isNotOver
    var registrationStarted = userService.isRegistrationStarted
    var maxNumberParticipants = userService.maxNumberParticipants
    maxNumberParticipants.map { num => Ok(views.html.index(registrationForm, notBookedOut, notOver, registrationStarted, num)) }
  }

  def addRegistration = Action.async { implicit request =>
    var notBookedOut = userService.isNotBookedOut
    var notOver = userService.isNotOver
    var registrationStarted = userService.isRegistrationStarted
    var maxNumberParticipants = userService.maxNumberParticipants
    registrationForm.bindFromRequest().fold(
      formWithErrors => userService.maxNumberParticipants.map { num => Ok(views.html.index(formWithErrors, notBookedOut, notOver, registrationStarted, num)) },
      registration => {
        registrationRepo.create(registration.name, registration.email).map { _ =>
          Ok(views.html.registration_successful(registration.name, registration.email))
        }
      })
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
