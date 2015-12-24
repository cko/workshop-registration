package controllers


import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.api.Environment
import play.mvc.Results.Redirect
import play.api.i18n.MessagesApi
import javax.inject.Inject
import scala.concurrent.Future
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import models.User
import forms.SignInForm

class ApplicationController @Inject() (
  val messagesApi: MessagesApi,
  val env: Environment[User, CookieAuthenticator])
  extends Silhouette[User, CookieAuthenticator]{
  
    def signIn = UserAwareAction.async { implicit request =>
    request.identity match {
      case Some(user) => Future.successful(Ok(views.html.signIn(SignInForm.form)))
      case None => Future.successful(Ok(views.html.signIn(SignInForm.form)))
    }
  }
    
}