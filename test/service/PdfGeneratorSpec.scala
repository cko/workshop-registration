package service

import integration.BaseIntegrationAppSpec
import play.api.Play

class PdfGeneratorSpec extends BaseIntegrationAppSpec {

  "PdfGenerator" must {
    "generate single pdf" in {
      var pdfGenerator = Play.current.injector.instanceOf(classOf[PdfGenerator])
      val pdfByteArray = pdfGenerator.generateSinglePdf("a workshop", "a name")
      
      pdfByteArray must not be null
    }
    "generate complete pdf" in {
      var pdfGenerator = Play.current.injector.instanceOf(classOf[PdfGenerator])
      val pdfByteArray = pdfGenerator.generateDownloadPdf(List("participant1", "participant2"), "workshop") 
      
      pdfByteArray must not be null
    }
  }

}