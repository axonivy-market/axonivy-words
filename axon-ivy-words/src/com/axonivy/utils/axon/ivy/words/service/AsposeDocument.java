package com.axonivy.utils.axon.ivy.words.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.aspose.words.Document;
import com.aspose.words.IMailMergeDataSource;
import com.aspose.words.SaveFormat;
import com.axonivy.utils.axon.ivy.words.service.mergefield.TemplateMergeField;

import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.scripting.objects.Recordset;

public class AsposeDocument extends BaseDocument {

  private Document doc;

  public AsposeDocument() {
    super();
  }

  @Override
  public File generateDocumentWithRegions(
      String templatePath,
      String outName,
      String outPath,
      List<TemplateMergeField> mergeFields,
      Hashtable<String, Recordset> hashtable) {

    return WordFactory.get(() -> {
      try {
        File template = validateTemplate(templatePath);
        if (template == null) {
          return null;
        }

        String safeOutName = prepareOutputName(outName);
        String safeOutPath = prepareOutputPath(outPath);
        List<TemplateMergeField> safeMergeFields = ensureMergeFields(mergeFields);

        this.doc = new Document(template.getAbsolutePath());

        mergeSimpleFields(safeMergeFields);

        mergeRegions(hashtable);

        return saveDocument(safeOutPath, safeOutName);

      } catch (Exception e) {
        Ivy.log().error("Error generating document", e);
        return null;
      }
    });
  }

  private File validateTemplate(String templatePath) {
    if (StringUtils.isBlank(templatePath)) {
      return null;
    }
    File template = new File(templatePath);
    return template.exists() ? template : null;
  }

  private String prepareOutputName(String outName) {
    return StringUtils.isBlank(outName)
        ? "serialLetter_" + System.currentTimeMillis()
        : outName;
  }

  private String prepareOutputPath(String outPath) {
    return StringUtils.isBlank(outPath)
        ? "ivy_RIA_files/"
        : outPath;
  }

  private List<TemplateMergeField> ensureMergeFields(List<TemplateMergeField> mergeFields) {
    return mergeFields == null ? new ArrayList<>() : mergeFields;
  }

  private void mergeSimpleFields(List<TemplateMergeField> mergeFields) throws Exception {
    String[] fieldNames = mergeFields.stream()
        .map(TemplateMergeField::getMergeFieldName)
        .toArray(String[]::new);
    Object[] fieldValues = mergeFields.stream()
        .map(TemplateMergeField::getValueForMailMerging)
        .toArray();
    doc.getMailMerge().execute(fieldNames, fieldValues);
  }

  private void mergeRegions(Hashtable<String, Recordset> hashtable) {
    if (hashtable == null) {
      return;
    }
    for (Map.Entry<String, Recordset> entry : hashtable.entrySet()) {
      try {
        IMailMergeDataSource ds = new MailMergeDataSource(entry.getValue(), entry.getKey());
        doc.getMailMerge().executeWithRegions(ds);
      } catch (Exception ex) {
        Ivy.log().error("Error merging table: " + entry.getKey(), ex);
      }
    }
  }

  private File saveDocument(String outPath, String outName) throws Exception {
    String outputFilePath = outPath + outName + ".docx";
    File outFile = new File(outputFilePath);
    doc.save(outputFilePath, SaveFormat.DOCX);
    return outFile;
  }

  @Override
  public boolean isFormatSupported(String format) {
    return StringUtils.isNotBlank(format)
        && (format.equalsIgnoreCase("docx") || format.equalsIgnoreCase("doc"));
  }

  public static String[] getSupportedFormats() {
    return new String[] { "docx", "doc" };
  }
}
