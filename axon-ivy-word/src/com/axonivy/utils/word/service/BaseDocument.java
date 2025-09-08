package com.axonivy.utils.word.service;

import java.io.File;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.axonivy.utils.word.service.mergefield.TemplateMergeField;

import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.scripting.objects.Recordset;

public abstract class BaseDocument {

  private static final String DOCUMENT_IMPLEMENTATION_SYSTEM_PROPERTY = "document";

  protected String outputPath;
  protected String outputFormat;
  protected File template;
  protected File fileResult;

  /** the list of supported formats */
  public static final String[] SUPPORTED_OUTPUT_FORMATS = new String[] {
      "doc", "docx", "html", "txt", "pdf", "odt"
  };

  public static final int UNSUPPORTED_FORMAT = DocumentConstants.UNSUPPORTED_FORMAT;
  public static final int DOC_FORMAT = DocumentConstants.DOC_FORMAT;
  public static final int DOCX_FORMAT = DocumentConstants.DOCX_FORMAT;
  public static final int PDF_FORMAT = DocumentConstants.PDF_FORMAT;

  public BaseDocument() {
    this.outputFormat = SUPPORTED_OUTPUT_FORMATS[0]; 
    this.outputPath = "defaultDirectory";
  }

  public static BaseDocument getInstance() {
    try {
      if (StringUtils.isBlank(System.getProperty(DOCUMENT_IMPLEMENTATION_SYSTEM_PROPERTY))) {
        return new AsposeDocument();
      } else {
        return (BaseDocument) Class
            .forName(System.getProperty("axonivy.document"))
            .getDeclaredConstructor()
            .newInstance();
      }
    } catch (Exception e) {
      Ivy.log().error("Exception generating the docFactory. " + e.getMessage(), e);
      return null;
    }
  }
  public abstract boolean isFormatSupported(String _format);

  public abstract File generateDocumentWithRegions(
      String templatePath,
      String outputName,
      String outPath,
      List<TemplateMergeField> mergefields,
      Hashtable<String, Recordset> hashtable);
  /**
   * get the supported file output formats
   * @return an array of Strings representing the supported formats
   */
  public static String[] getSupportedFormats() {
    return SUPPORTED_OUTPUT_FORMATS;
  }
}
