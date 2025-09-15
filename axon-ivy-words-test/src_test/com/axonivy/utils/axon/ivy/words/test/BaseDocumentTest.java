package com.axonivy.utils.axon.ivy.words.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Hashtable;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import com.axonivy.utils.axon.ivy.words.service.AsposeDocument;
import com.axonivy.utils.axon.ivy.words.service.BaseDocument;
import com.axonivy.utils.axon.ivy.words.service.mergefield.TemplateMergeField;

import ch.ivyteam.ivy.environment.IvyTest;
import ch.ivyteam.ivy.scripting.objects.Recordset;

@IvyTest
public class BaseDocumentTest {
  private static final String TEST_DIRECTORY_NAME = "test_directory";

  @AfterEach
  public void clean_generated_test_documents() {
    java.io.File directory_for_testing = new java.io.File(TEST_DIRECTORY_NAME);
    if (directory_for_testing.isDirectory()) {
      for (java.io.File f : directory_for_testing.listFiles()) {
        f.delete();
      }
      directory_for_testing.delete();
    }
  }

  @Test
  public void getInstance_return_not_null() {
    BaseDocument document = BaseDocument.getInstance();
    assertThat(document).isNotNull();
  }

  @Test
  public void supportedFormats_shouldContainDocAndDocx() {
    String[] formats = BaseDocument.getSupportedFormats();
    assertThat(formats).containsExactly("doc", "docx");
  }

  @Test
  public void generateDocumentWithRegions_withInvalidTemplate_shouldReturnNull() {
    BaseDocument document = new AsposeDocument();
    java.io.File result = document.generateDocumentWithRegions("not_existing_file.docx", "output", TEST_DIRECTORY_NAME,
        List.of(new TemplateMergeField("name", "value")), new Hashtable<String, Recordset>());
    assertThat(result).isNull();
  }
}
