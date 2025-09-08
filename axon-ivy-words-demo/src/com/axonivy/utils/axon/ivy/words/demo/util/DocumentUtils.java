package com.axonivy.utils.axon.ivy.words.demo.util;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import org.primefaces.model.file.UploadedFile;

import com.aspose.words.SaveFormat;
import com.axonivy.utils.axon.ivy.word.service.Format;
import com.axonivy.utils.axon.ivy.word.service.WordFactory;

import ch.ivyteam.ivy.environment.Ivy;

public class DocumentUtils {
  public static File getFileFromPath(String filePath) throws URISyntaxException {
    URL url = DocumentUtils.class.getResource(filePath);
    if (url == null) {
      throw new RuntimeException("Resource not found!");
    }
    return new File(url.toURI());
  }

  /**
   * Converts a document to the specified format using the legacy approach.
   * This method is kept for backward compatibility.
   * 
   * @param saveFormat the Aspose SaveFormat constant
   * @param file the uploaded file to convert
   * @return the converted document as byte array
   * @deprecated Use {@link #convertToFormat(Format, UploadedFile)} instead
   */
  @Deprecated
  public static byte[] convertTo(int saveFormat, UploadedFile file) {
    try (InputStream inputStream = file.getInputStream()) {
      // Convert Aspose SaveFormat to our Format enum
      Format format = getFormatFromAsposeConstant(saveFormat);
      return WordFactory.convert().from(inputStream).to(format).asBytes();
    } catch (Exception e) {
      Ivy.log().error(e);
      return new byte[0];
    }
  }

  /**
   * Converts a document to the specified format using the new fluent API.
   * 
   * @param format the target format
   * @param file the uploaded file to convert
   * @return the converted document as byte array
   */
  public static byte[] convertToFormat(Format format, UploadedFile file) {
    try (InputStream inputStream = file.getInputStream()) {
      return WordFactory.convert().from(inputStream).to(format).asBytes();
    } catch (Exception e) {
      Ivy.log().error(e);
      return new byte[0];
    }
  }

  /**
   * Converts a document to PDF using the new fluent API.
   * 
   * @param file the uploaded file to convert
   * @return the converted PDF document as byte array
   */
  public static byte[] convertToPdf(UploadedFile file) {
    try (InputStream inputStream = file.getInputStream()) {
      return WordFactory.convert().from(inputStream).toPdf().asBytes();
    } catch (Exception e) {
      Ivy.log().error(e);
      return new byte[0];
    }
  }

  private static Format getFormatFromAsposeConstant(int saveFormat) {
    if (saveFormat == SaveFormat.PDF) return Format.PDF;
    if (saveFormat == SaveFormat.DOCX) return Format.DOCX;
    if (saveFormat == SaveFormat.DOC) return Format.DOC;
    if (saveFormat == SaveFormat.RTF) return Format.RTF;
    if (saveFormat == SaveFormat.TEXT) return Format.TXT;
    if (saveFormat == SaveFormat.HTML) return Format.HTML;
    if (saveFormat == SaveFormat.EPUB) return Format.EPUB;
    if (saveFormat == SaveFormat.ODT) return Format.ODT;
    if (saveFormat == SaveFormat.XPS) return Format.XPS;
    return Format.PDF; // default fallback
  }
}