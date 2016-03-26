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
import scala.inline
import service.PdfGenerator
import scala.inline
import scala.inline
import scala.inline
import java.io.File
import scala.inline
import java.io.FileOutputStream

class AdministrationController @Inject() (repo: RegistrationRepository, val pdfGenerator: PdfGenerator, val messagesApi: MessagesApi, val env: Environment[User, CookieAuthenticator])
                                          (implicit ec: ExecutionContext) extends Silhouette[User, CookieAuthenticator] with I18nSupport {

  def getRegistrations = SecuredAction.async {
    repo.list().map { registrations =>
      Ok(views.html.registered(registrations))
    }
  }
  /*
  def getConfirmationPdf = SecuredAction.async{
    val participants = List("")
    //FIXME
    val workshop = "Dummy-workshop"
    val pdfOutputStream = pdfGenerator.generateDownloadPdf(participants, workshop)
    val pdfContent = new File("")  
    
     var fos = new FileOutputStream (pdfContent); 
     pdfOutputStream.writeTo(fos);
  
    Ok.sendFile(pdfContent, false, fileName = _ => "teilnahmebestaetigungen.pdf")
  }*/
}

