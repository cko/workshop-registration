package service

import integration.BaseIntegrationSpec
import play.api.Play

class PdfGeneratorSpec extends BaseIntegrationSpec {

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