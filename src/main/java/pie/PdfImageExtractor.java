package pie;

import java.io.File;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;
import org.apache.pdfbox.util.PDFTextStripper;
import org.joda.time.format.DateTimeFormat;

public class PdfImageExtractor {

  public void extractAll(File sourceDir, File destinationDir) throws Exception {
    Collection<File> files = FileUtils.listFiles(sourceDir, new String[] { "pdf" }, false);
    for (File file : files) {
      if (file.getName().contains("Daily Report")) {
        continue;
      }
      extractImage(file, destinationDir);
    }
  }

  private void extractImage(File pdfFile, File destinationDir) throws Exception {
    try (PDDocument document = PDDocument.load(pdfFile);) {
      PDPage page = (PDPage) document.getDocumentCatalog().getAllPages().get(0);
      PDResources pdResources = page.getResources();
      Map<String, PDXObject> pageImages = pdResources.getXObjects();
      PDXObjectImage pdxObjectImage = (PDXObjectImage) pageImages.get("X2");
      String date = extractDate(document);
      File imageFile = new File(destinationDir, buildFilename(pdfFile.getName(), date));
      pdxObjectImage.write2file(imageFile);
    }
  }

  private String extractDate(PDDocument pdDoc) throws Exception {
    PDFTextStripper pdfStripper = new PDFTextStripper("utf8");
    pdfStripper.setStartPage(1);
    pdfStripper.setEndPage(5);
    String parsedText = pdfStripper.getText(pdDoc);
    String dateText = StringUtils.substringBefore(parsedText.split("\n")[5], " at");
    dateText = StringUtils.replace(dateText, " ", " ");
    dateText = DateTimeFormat.forPattern("MMMM dd, yyyy").parseDateTime(dateText).toString("yyyy-MM-dd");
    return dateText;
  }

  private String buildFilename(String pdfFileName, String date) {
    String name = StringUtils.substringBefore(pdfFileName, "-").replace("[Email]", "").trim();
    String subject = StringUtils.substringAfter(pdfFileName, "-").replace(".pdf", "").trim();
    return String.format("%s %s %s.jpg", date, name, subject);
  }
}
