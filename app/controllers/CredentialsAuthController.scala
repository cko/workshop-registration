package controllers

import javax.inject.Inject

import com.mohiva.play.silhouette.api.Authenticator.Implicits._
import com.mohiva.play.silhouette.api._
import com.mohiva.play.silhouette.api.exceptions.ProviderException
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.{ Clock, Credentials }
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import com.mohiva.play.silhouette.impl.exceptions.IdentityNotFoundException
import com.mohiva.play.silhouette.impl.providers._
import forms.SignInForm
import models.User
import net.ceedubs.ficus.Ficus._
import play.api.Configuration
import play.api.i18n.{ Messages, MessagesApi }
import play.api.libs.concurrent.Execution.Implicits._
import play.api.mvc.Action
import service.UserService

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.language.postfixOps


class CredentialsAuthController @Inject() (
                                            val messagesApi: MessagesApi,
                                            val env: Environment[User, CookieAuthenticator],
                                            userService: UserService,
                                            authInfoRepository: AuthInfoRepository,
                                            credentialsProvider: CredentialsProvider,
                                            configuration: Configuration,
                                            clock: Clock)
  extends Silhouette[User, CookieAuthenticator] {


  def authenticate = Action.async { implicit request =>
    SignInForm.form.bindFromRequest.fold(
      form => Future.successful(BadRequest(views.html.signIn(form))),
      data => {
        val credentials = Credentials(data.email, data.password)
        credentialsProvider.authenticate(credentials).flatMap { loginInfo =>
          val result = Redirect(controllers.admin.routes.AdministrationController.getRegistrations())
          userService.retrieve(loginInfo).flatMap {
            case Some(user) =>
              val c = configuration.underlying
              env.authenticatorService.create(loginInfo).map {
                case authenticator if data.rememberMe =>
                  authenticator.copy(
                    expirationDateTime = clock.now + c.as[FiniteDuration]("silhouette.authenticator.rememberMe.authenticatorExpiry"),
                    idleTimeout = c.getAs[FiniteDuration]("silhouette.authenticator.rememberMe.authenticatorIdleTimeout"),
                    cookieMaxAge = c.getAs[FiniteDuration]("silhouette.authenticator.rememberMe.cookieMaxAge")
                  )
                case authenticator => authenticator
              }.flatMap { authenticator =>
                env.eventBus.publish(LoginEvent(user, request, request2Messages))
                env.authenticatorService.init(authenticator).flatMap { v =>
                  env.authenticatorService.embed(v, result)
                }
              }
            case None => Future.failed(new IdentityNotFoundException("Couldn't find user"))
          }
        }.recover {
          case e: ProviderException =>
            Redirect(routes.ApplicationController.signIn()).flashing("error" -> Messages("invalid.credentials"))
        }
      }
    )
  }
}
