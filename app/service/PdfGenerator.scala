package service

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import java.io.File
import org.apache.pdfbox.multipdf.PDFMergerUtility
import java.io.ByteArrayOutputStream
import java.io.ByteArrayInputStream
import play.Play
import play.api.Logger


class PdfGenerator {
  
  /*
   def concatDocuments = {
    var joined = new PDFMergerUtility()
    //joined.addSource(generatePdf("Workshop 1", "name 1"))
    //joined.addSource(generatePdf("Workshop 2", "name 2"))
    joined.addSource(new ByteArrayInputStream(generatePdf("Workshop 1", "name 1")))
    joined.addSource(new ByteArrayInputStream(generatePdf("Workshop 2", "name 2")))
    //joined.setDestinationFileName("merged.pdf")
    var colDocOutputstream = new ByteArrayOutputStream()
    joined.setDestinationStream(colDocOutputstream)
    joined.mergeDocuments(null)
  }*/

  
  def generatePdf(workshop: String, name: String): Array[Byte] = {
    val formSource = Play.application.resourceAsStream("teilnehmer_formular.pdf")
    var document = PDDocument.load(formSource)

    var acroForm = document.getDocumentCatalog().getAcroForm()
    if (acroForm != null) {
      var workshopField = acroForm.getField("workshop")
      workshopField.setValue(workshop)

      var participantField = acroForm.getField("teilnehmer")
      participantField.setValue(name)
    }

    var baos = new ByteArrayOutputStream();
    document.save(baos);
    document.close()
    return baos.toByteArray();
  }
  
}