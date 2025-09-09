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
        try {
            WordFactory.loadLicense();
        } catch (Exception e) {
            Ivy.log().error("Aspose Words Licence error", e);
        }
    }

    @Override
    public File generateDocumentWithRegions(String templatePath, String outName, String outPath,
                                            List<TemplateMergeField> mergeFields, Hashtable<String, Recordset> hashtable) {
        try {
            if (StringUtils.isBlank(templatePath)) {
                return null;
            }

            File template = new File(templatePath);
            if (!template.exists()) {
                return null;
            }

            if (mergeFields == null) {
                mergeFields = new ArrayList<>();
            }
            if (StringUtils.isBlank(outName)) {
                outName = "serialLetter_" + System.currentTimeMillis();
            }
            if (StringUtils.isBlank(outPath)) {
                outPath = "ivy_RIA_files/";
            }

            doc = new Document(template.getAbsolutePath());

            String[] fieldNames = mergeFields.stream()
                    .map(TemplateMergeField::getMergeFieldName)
                    .toArray(String[]::new);
            Object[] fieldValues = mergeFields.stream()
                    .map(TemplateMergeField::getValueForMailMerging)
                    .toArray();

            doc.getMailMerge().execute(fieldNames, fieldValues);
            if (hashtable != null) {
                for (Map.Entry<String, Recordset> entry : hashtable.entrySet()) {
                    try {
                        IMailMergeDataSource ds = new MailMergeDataSource(entry.getValue(), entry.getKey());
                        doc.getMailMerge().executeWithRegions(ds);
                    } catch (Exception ex) {
                        Ivy.log().error("Error merging table: " + entry.getKey(), ex);
                    }
                }
            }

            String outputFilePath = outPath + outName + ".docx";
            Ivy.log().info("output file generate:"+outputFilePath);
            File outFile = new File(outputFilePath);
            doc.save(outputFilePath, SaveFormat.DOCX);

            return outFile;

        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean isFormatSupported(String format) {
        return StringUtils.isNotBlank(format) &&
               (format.equalsIgnoreCase("docx") || format.equalsIgnoreCase(".docx"));
    }

    public static String[] getSupportedFormats() {
        return new String[] {"docx"};
    }
}
