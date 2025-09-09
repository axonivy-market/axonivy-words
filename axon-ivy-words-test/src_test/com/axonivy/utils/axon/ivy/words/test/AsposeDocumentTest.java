package com.axonivy.utils.axon.ivy.words.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import com.axonivy.utils.axon.ivy.words.service.AsposeDocument;
import com.axonivy.utils.axon.ivy.words.service.mergefield.TemplateMergeField;

import ch.ivyteam.ivy.scripting.objects.Recordset;
public class AsposeDocumentTest {

    private static final String TEST_TEMPLATE_PATH = "test-template.docx";
    private static final String TEST_OUTPUT_DIR = "test-output/";

    private AsposeDocument asposeDocument = new AsposeDocument();

    @AfterEach
    public void cleanup() {
        File template = new File(TEST_TEMPLATE_PATH);
        if (template.exists()) {
            template.delete();
        }
        File outputDir = new File(TEST_OUTPUT_DIR);
        if (outputDir.isDirectory()) {
            for (File f : outputDir.listFiles()) {
                f.delete();
            }
            outputDir.delete();
        }
    }

    @Test
    void generateDocumentWithRegions_shouldReturnNull_whenTemplatePathBlank() {
        File result = asposeDocument.generateDocumentWithRegions(
                "", "output", TEST_OUTPUT_DIR, null, null
        );
        assertThat(result).isNull();
    }

    @Test
    void generateDocumentWithRegions_shouldReturnNull_whenTemplateDoesNotExist() {
        File result = asposeDocument.generateDocumentWithRegions(
                "not-exist.docx", "output", TEST_OUTPUT_DIR, null, null
        );
        assertThat(result).isNull();
    }

    @Test
    void generateDocumentWithRegions_shouldCreateFile_whenTemplateExists() throws Exception {
        File fakeTemplate = new File(TEST_TEMPLATE_PATH);
        try (FileWriter writer = new FileWriter(fakeTemplate)) {
            writer.write("Fake Word Content");
        }

        List<TemplateMergeField> mergeFields = new ArrayList<>();
        mergeFields.add(new TemplateMergeField("name", "John Doe"));

        File result = asposeDocument.generateDocumentWithRegions(
                TEST_TEMPLATE_PATH, "test-generated", TEST_OUTPUT_DIR, mergeFields, new Hashtable<String, Recordset>()
        );

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("test-generated.docx");
        assertThat(result).exists();
    }

    @Test
    void isFormatSupported_shouldReturnTrueForDocx() {
        assertThat(asposeDocument.isFormatSupported("docx")).isTrue();
        assertThat(asposeDocument.isFormatSupported(".docx")).isTrue();
    }

    @Test
    void isFormatSupported_shouldReturnFalseForOtherFormats() {
        assertThat(asposeDocument.isFormatSupported("pdf")).isFalse();
        assertThat(asposeDocument.isFormatSupported("")).isFalse();
        assertThat(asposeDocument.isFormatSupported(null)).isFalse();
    }

    @Test
    void getSupportedFormats_shouldContainDocx() {
        String[] formats = AsposeDocument.getSupportedFormats();
        assertThat(formats).containsExactly("docx");
    }
}