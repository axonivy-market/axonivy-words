package com.axonivy.utils.axonivywords.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;

import com.aspose.words.Document;
import com.aspose.words.SaveFormat;
import com.axonivy.utils.axonivywords.service.DocumentConversionException;
import com.axonivy.utils.axonivywords.service.DocumentConverter;
import com.axonivy.utils.axonivywords.service.WordFactory;

import ch.ivyteam.ivy.environment.IvyTest;

@IvyTest
class DocumentConverterTest {
  private final String TEST_FILE_PATH = "src_test/resources/demo.docx";
  private final String TEST_OUTPUT_FILE_PATH = "src_test/resources/output.docx";

  @BeforeEach
  void resetLicenseField() throws Exception {
    var field = WordFactory.class.getDeclaredField("license");
    field.setAccessible(true);
    field.set(null, null);
  }

  @Test
  void testConvertFromInputStreamToPdfAsBytes() throws Exception {
    byte[] testData = "test document content".getBytes();
    InputStream inputStream = new ByteArrayInputStream(testData);
    byte[] result = WordFactory.convert().from(inputStream).toPdf().asBytes();
    assertNotNull(result);
    assertTrue(result.length > 0);
  }

  @Test
  void testConvertFromFileToFormatAsFile() throws Exception {
    File inputFile = new File(TEST_FILE_PATH);
    File result = WordFactory.convert().from(inputFile).to(SaveFormat.PDF).asFile(TEST_OUTPUT_FILE_PATH);
    assertNotNull(result);
  }

  @Test
  void testConvertFromFilePathToPdfAsInputStream() throws Exception {
    InputStream result = WordFactory.convert().from(TEST_FILE_PATH).toPdf().asInputStream();
    assertNotNull(result);
  }

  @Test
  void testConvertFromBytesArrayToPdfAsBytes() throws Exception {
    byte[] inputBytes = "test document content".getBytes();
    byte[] result = WordFactory.convert().from(inputBytes).toPdf().asBytes();
    assertNotNull(result);
    assertTrue(result.length > 0);
  }

  @Test
  void testConvertWithoutSourceThrowsException() {
    DocumentConverter converter = WordFactory.convert();
    assertThrows(IllegalStateException.class, () -> {
      converter.toPdf().asBytes();
    });
  }

  @Test
  void testConvertFromInvalidInputStreamThrowsException() throws Exception {
    withMockedDocumentFailure(() -> {
      InputStream inputStream = new ByteArrayInputStream("invalid content".getBytes());
      assertThrows(DocumentConversionException.class, () -> {
        WordFactory.convert().from(inputStream).toPdf().asBytes();
      });
    });
  }

  @Test
  void testConvertFromInvalidFileThrowsException() throws Exception {
    withMockedDocumentFailure(() -> {
      File invalidFile = new File("nonexistent.docx");
      assertThrows(DocumentConversionException.class, () -> {
        WordFactory.convert().from(invalidFile).toPdf().asBytes();
      });
    });
  }

  private void withMockedDocumentFailure(Runnable test) throws Exception {
    try (MockedConstruction<Document> mockedDocumentConstructor = Mockito.mockConstruction(Document.class,
        (mock, context) -> {
          throw new RuntimeException("Document creation failed");
        })) {

      test.run();
    }
  }
}
