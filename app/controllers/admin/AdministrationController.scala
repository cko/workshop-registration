package controllers.admin

import scala.annotation.implicitNotFound
import scala.concurrent.ExecutionContext

import com.mohiva.play.silhouette.api.Environment
import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator

import dal.RegistrationRepository
import dal.WorkshopRepository
import javax.inject.Inject
import javax.inject.Singleton
import models.User
import play.api.i18n.I18nSupport
import play.api.i18n.MessagesApi
import service.PdfGenerator

class AdministrationController @Inject() (
    val registrationRepo: RegistrationRepository,
    val workshopRepo: WorkshopRepository,
    val pdfGenerator: PdfGenerator,
    val messagesApi: MessagesApi,
    val env: Environment[User, CookieAuthenticator])(implicit ec: ExecutionContext) extends Silhouette[User, CookieAuthenticator] with I18nSupport {

  def getRegistrations = SecuredAction.async {
    workshopRepo.active().flatMap { workshop => {
      registrationRepo.list(workshop.id.get).map { registrations =>
        Ok(views.html.registered(registrations))
      }
    } }
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

