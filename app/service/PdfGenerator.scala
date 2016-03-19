package service

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import java.io.File
import org.apache.pdfbox.multipdf.PDFMergerUtility
import java.io.ByteArrayOutputStream
import java.io.ByteArrayInputStream
import play.Play
import play.api.Logger
import scala.collection.immutable.List


class PdfGenerator {
  
   def generateDownloadPdf (participants : List[String], workshop: String): ByteArrayOutputStream = {
    var joined = new PDFMergerUtility()
    participants.foreach { p => joined.addSource(new ByteArrayInputStream(generateSinglePdf(workshop, p))) }    
    var colDocOutputstream = new ByteArrayOutputStream()
    joined.setDestinationStream(colDocOutputstream)
    joined.mergeDocuments(null)
    colDocOutputstream
  }

  
  def generateSinglePdf(workshop: String, name: String): Array[Byte] = {
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
    baos.toByteArray();
  }
  
}