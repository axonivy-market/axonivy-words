package com.axonivy.utils.axon.ivy.words.demo.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import org.primefaces.model.file.UploadedFile;

import com.aspose.words.DocSaveOptions;
import com.aspose.words.Document;
import com.aspose.words.SaveOptions;
import com.axonivy.utils.axon.ivy.words.service.WordFactory;

import ch.ivyteam.ivy.environment.Ivy;

public class DocumentUtils {
  public static File getFileFromPath(String filePath) throws URISyntaxException {
    URL url = DocumentUtils.class.getResource(filePath);
    if (url == null) {
      throw new RuntimeException("Resource not found!");
    }
    return new File(url.toURI());
  }

  public static byte[] convertTo(int saveFormat, UploadedFile file) {
    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = file.getInputStream()) {
      WordFactory.loadLicense();
      Document doc = new Document(inputStream);
      SaveOptions options = DocSaveOptions.createSaveOptions(saveFormat);
      doc.save(outputStream, options);
      return outputStream.toByteArray();
    } catch (Exception e) {
      Ivy.log().error(e);
      return new byte[0];
    }
  }
}