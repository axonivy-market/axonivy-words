package com.axonivy.connector.word.demo.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import com.aspose.words.DocSaveOptions;
import com.aspose.words.Document;
import com.aspose.words.SaveOptions;
import com.axonivy.connector.word.service.WordFactory;

import ch.ivyteam.ivy.environment.Ivy;

public class DocumentUtils {
	public static File getFileFromPath(String filePath) throws URISyntaxException {
		URL url = DocumentUtils.class.getResource(filePath);
		if (url == null) {
			throw new RuntimeException("Resource not found!");
		}
		return new File(url.toURI());
	}

	public static byte[] convertTo(int saveFormat, String filePath) {
		try (InputStream inputStream = new FileInputStream(DocumentUtils.getFileFromPath(filePath));
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
}
