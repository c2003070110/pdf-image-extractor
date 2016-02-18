package pie;

import java.io.File;

public class Main {

  public static void main(String... args) throws Exception {
    File sourceDir = new File(args[0]);
    File destinationDir = new File(args[1]);
    PdfImageExtractor pdfImageExtractor = new PdfImageExtractor();
    pdfImageExtractor.extractAll(sourceDir, destinationDir);
  }
}
