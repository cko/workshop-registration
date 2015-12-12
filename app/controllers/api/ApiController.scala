package controllers.api

import javax.inject.Inject

import dal.RegistrationRepository
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext


class ApiController @Inject() (repo: RegistrationRepository)(implicit ec: ExecutionContext) extends Controller {

  def getRegistrations = Action.async {
    repo.list().map { registrations =>
      Ok(Json.toJson(registrations));
    }
  }
}
