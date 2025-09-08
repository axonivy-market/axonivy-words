package com.axonivy.utils.word.service.preview;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.aspose.words.Document;
import com.aspose.words.LoadFormat;
import com.aspose.words.LoadOptions;
import com.aspose.words.SaveFormat;
import com.axonivy.utils.word.service.WordFactory;
import com.axonivy.utils.word.service.DocumentConstants;

public class DocumentPreviewService {

	private static final DocumentPreviewService INSTANCE = new DocumentPreviewService();

	private DocumentPreviewService() {
	}

	public static DocumentPreviewService getInstance() {
		return INSTANCE;
	}

	public StreamedContent generateStreamedContent(File file) throws Exception {
		String fileName = file.getName();
		String contentType = Files.probeContentType(file.toPath());
		byte[] fileContent = getDataFromFile(file);
		return convertToStreamContent(fileName, fileContent, contentType);
	}

	private StreamedContent convertToStreamContent(String fileName, byte[] fileContent, String contentType)
			throws Exception {
		WordFactory.loadLicense();
		return convertWordToPdfStreamedContent(fileContent, fileName);
	}

	private byte[] getDataFromFile(File file) {
		try (FileInputStream fis = new FileInputStream(file)) {
			return fis.readAllBytes();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private StreamedContent convertWordToPdfStreamedContent(byte[] data, String fileName) throws Exception {
	    try (InputStream inputStream = new ByteArrayInputStream(data);
	        ByteArrayOutputStream pdfOut = new ByteArrayOutputStream()) {
	      LoadOptions loadOptions = new LoadOptions();
	      loadOptions.setLoadFormat(LoadFormat.AUTO);
	      Document document = new Document(inputStream, loadOptions);
	      document.save(pdfOut, SaveFormat.PDF);
	      return convertOutputStreamToStreamedContent(pdfOut, fileName);
	    }
	  }

	private StreamedContent convertOutputStreamToStreamedContent(ByteArrayOutputStream pdfOut, String fileName) {
	    byte[] pdfBytes = pdfOut.toByteArray();
	    return convertOutputStreamToStreamedContent(fileName, DocumentConstants.PDF_CONTENT_TYPE, pdfBytes);
	  }

	  private StreamedContent convertOutputStreamToStreamedContent(String fileName, String contentType,
	      byte[] fileContent) {
	    return DefaultStreamedContent.builder().contentType(contentType).name(fileName)
	        .stream(() -> new ByteArrayInputStream(fileContent)).build();
	  }
}
