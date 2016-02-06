package controllers

import com.mohiva.play.silhouette.api.Environment
import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import javax.inject.Inject
import models.User
import play.api.i18n.MessagesApi
import forms.SignInForm
import scala.concurrent.Future
import play.mvc.Results.Redirect
import com.mohiva.play.silhouette.api.LogoutEvent

class ApplicationController @Inject() (
  val messagesApi: MessagesApi,
  val env: Environment[User, CookieAuthenticator])
  extends Silhouette[User, CookieAuthenticator]{

  def signIn = UserAwareAction.async { implicit request =>
    request.identity match {
      case Some(user) => Future.successful(Redirect("admin/registrations"))
      case None       => Future.successful(Ok(views.html.signIn(SignInForm.form)))
    }
  }
    
    def signOut = SecuredAction.async { implicit request =>
      val result = Redirect(controllers.user.routes.RegistrationController.index())
      env.eventBus.publish(LogoutEvent(request.identity, request, request2Messages))

      env.authenticatorService.discard(request.authenticator, result)
    }
    
}