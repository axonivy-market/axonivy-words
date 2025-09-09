package com.axonivy.utils.axon.ivy.words.demo;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.axonivy.utils.axon.ivy.words.service.DocumentConstants;
import com.axonivy.utils.axon.ivy.words.service.WordFactory;

public class ConvertDocumentPdfFile {

  public static StreamedContent convert(String filePath, String fileName) throws IOException {
    if (filePath == null || filePath.trim().isEmpty()) {
      throw new IllegalArgumentException("File path cannot be null or empty");
    }

    File file = new File(filePath);
    if (!file.exists()) {
      throw new IOException("File not found: " + filePath);
    }

    byte[] fileContent = Files.readAllBytes(file.toPath());
    String pdfFileName = fileName.replaceAll("\\.docx$", "") + ".pdf";

    return DefaultStreamedContent.builder().name(pdfFileName).contentType(DocumentConstants.PDF_CONTENT_TYPE)
        .stream(() -> new ByteArrayInputStream(WordFactory.convert().from(fileContent).toPdf().asBytes())).build();
  }
}
