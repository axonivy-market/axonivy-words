package com.axonivy.utils.axonivywords.demo;

import java.io.ByteArrayInputStream;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.file.UploadedFile;

import com.axonivy.utils.axonivywords.service.WordFactory;

public class ConvertDocumentService {
  private static final String PDF_EXTENSION = ".pdf";
  private static final String DOT = ".";

  public static DefaultStreamedContent convert(UploadedFile uploadedFile) {
    if (uploadedFile != null) {
      String pdfFileName = updateFileExtension(uploadedFile.getFileName());
      return DefaultStreamedContent.builder().name(pdfFileName).contentType("application/pdf")
          .stream(
              () -> new ByteArrayInputStream(WordFactory.convert().from(uploadedFile.getContent()).toPdf().asBytes()))
          .build();
    }
    return DefaultStreamedContent.builder().build();
  }

  private static String updateFileExtension(String filename) {
    String baseName = filename != null && filename.contains(DOT) ? filename.substring(0, filename.lastIndexOf(DOT)) : filename;
    return baseName + PDF_EXTENSION;
  }
}