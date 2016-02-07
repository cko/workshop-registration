package service

import integration.BaseIntegrationSpec
import play.api.Play

class PdfGeneratorSpec extends BaseIntegrationSpec {

  "PdfGenerator" must {
    "generate pdf" in {
      var pdfGenerator = Play.current.injector.instanceOf(classOf[PdfGenerator])
      val pdfByteArray = pdfGenerator.generatePdf("a workshop", "a name")
      
      pdfByteArray must not be null
    }
  }

}