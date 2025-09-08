package com.axonivy.utils.word.demo.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import javax.faces.context.FacesContext;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.aspose.words.DocSaveOptions;
import com.aspose.words.Document;
import com.aspose.words.SaveFormat;
import com.aspose.words.SaveOptions;
import com.axonivy.utils.word.service.DocumentConstants;
import com.axonivy.utils.word.service.WordFactory;

import ch.ivyteam.ivy.environment.Ivy;

public class DocumentUtils {
	public static File getFileFromPath(String filePath) throws URISyntaxException {
        URL url = DocumentUtils.class.getResource(filePath.startsWith("/") ? filePath : "/" + filePath);
        if (url != null) {
            return new File(url.toURI());
        }

        try {
            if (FacesContext.getCurrentInstance() != null) {
                String realPath = FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getRealPath(filePath);
                if (realPath != null) {
                    return new File(realPath);
                }
            }
        } catch (Exception ignored) {
        }

        File file = new File(filePath);
        if (file.exists()) {
            return file;
        }

        throw new RuntimeException("Resource not found at: " + filePath);
    }

  public static byte[] convertTo(int saveFormat, String filePath) {
    try (InputStream inputStream = new FileInputStream(getFileFromPath(filePath));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
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
  public static StreamedContent getPDFFile(String filePath) {
	    WordFactory.loadLicense();
	    return DefaultStreamedContent.builder().name("demo.pdf").contentType(DocumentConstants.PDF_CONTENT_TYPE)
	        .stream(() -> new ByteArrayInputStream(convertTo(SaveFormat.PDF, filePath)))
	        .build();
	  }
}
