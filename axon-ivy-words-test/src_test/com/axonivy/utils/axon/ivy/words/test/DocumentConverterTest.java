package com.axonivy.utils.axon.ivy.words.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.aspose.words.Document;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;
import com.axonivy.utils.axon.ivy.words.service.DocumentConversionException;
import com.axonivy.utils.axon.ivy.words.service.DocumentConverter;
import com.axonivy.utils.axon.ivy.words.service.WordFactory;

import ch.ivyteam.ivy.ThirdPartyLicenses;
import ch.ivyteam.ivy.environment.IvyTest;

@IvyTest
class DocumentConverterTest {

	@BeforeEach
	void resetLicenseField() throws Exception {
		var field = WordFactory.class.getDeclaredField("license");
		field.setAccessible(true);
		field.set(null, null);
	}

	@Test
	void testConvertFromInputStreamToPdfAsBytes() throws Exception {
		withMockedEnvironment(() -> {
			byte[] testData = "test document content".getBytes();
			InputStream inputStream = new ByteArrayInputStream(testData);

			byte[] result = WordFactory.convert().from(inputStream).toPdf().asBytes();

			assertNotNull(result);
			assertTrue(result.length > 0);
		});
	}

	@Test
	void testConvertFromFileToFormatAsFile() throws Exception {
		withMockedEnvironment(() -> {
			File inputFile = new File("test.docx");
			String outputPath = "output.pdf";

			File result = WordFactory.convert().from(inputFile).to(SaveFormat.PDF).asFile(outputPath);

			assertNotNull(result);
		});
	}

	@Test
	void testConvertFromFilePathToPdfAsInputStream() throws Exception {
		withMockedEnvironment(() -> {
			String inputPath = "/path/to/test.docx";

			InputStream result = WordFactory.convert().from(inputPath).toPdf().asInputStream();

			assertNotNull(result);
		});
	}

	@Test
	void testConvertFromBytesArrayToPdfAsBytes() throws Exception {
		withMockedEnvironment(() -> {
			byte[] inputBytes = "test document content".getBytes();

			byte[] result = WordFactory.convert().from(inputBytes).toPdf().asBytes();

			assertNotNull(result);
			assertTrue(result.length > 0);
		});
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

	private void withMockedEnvironment(Runnable test) throws Exception {
		try (MockedStatic<ThirdPartyLicenses> mockedThirdParty = Mockito.mockStatic(ThirdPartyLicenses.class)) {
			InputStream dummyLicenseStream = mock(InputStream.class);
			mockedThirdParty.when(ThirdPartyLicenses::getDocumentFactoryLicense).thenReturn(dummyLicenseStream);

			try (MockedConstruction<License> mockedLicenseConstructor = Mockito.mockConstruction(License.class)) {

				try (MockedConstruction<Document> mockedDocumentConstructor = Mockito.mockConstruction(Document.class)) {

					test.run();
				}
			}
		}
	}

	private void withMockedDocumentFailure(Runnable test) throws Exception {
		try (MockedStatic<ThirdPartyLicenses> mockedThirdParty = Mockito.mockStatic(ThirdPartyLicenses.class)) {
			InputStream dummyLicenseStream = mock(InputStream.class);
			mockedThirdParty.when(ThirdPartyLicenses::getDocumentFactoryLicense).thenReturn(dummyLicenseStream);

			try (MockedConstruction<License> mockedLicenseConstructor = Mockito.mockConstruction(License.class,
					(mock, context) -> {
						try {
							doNothing().when(mock).setLicense(any(InputStream.class));
						} catch (Exception e) {
							// Ignore mocking exceptions for License
						}
					})) {

				try (MockedConstruction<Document> mockedDocumentConstructor = Mockito.mockConstruction(Document.class,
						(mock, context) -> {
							throw new RuntimeException("Document creation failed");
						})) {

					test.run();
				}
			}
		}
	}
}
