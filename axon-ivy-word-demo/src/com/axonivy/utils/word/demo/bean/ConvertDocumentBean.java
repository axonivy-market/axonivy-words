package com.axonivy.utils.word.demo.bean;

import java.io.ByteArrayInputStream;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.aspose.words.SaveFormat;
import com.axonivy.utils.word.demo.util.DocumentUtils;
import com.axonivy.utils.word.service.DocumentConstants;
import com.axonivy.utils.word.service.WordFactory;

@ManagedBean
@ViewScoped
public class ConvertDocumentBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private final String DEFAULT_SAMPLE_FILE_PATH = "/resources/demo.docx";
	private final String CONVERTED_DEMO_FILE_NAME = "demo.pdf";
	private StreamedContent content;

	public StreamedContent getConvertedFile() {
		WordFactory.loadLicense();
		return DefaultStreamedContent.builder().name(CONVERTED_DEMO_FILE_NAME)
				.contentType(DocumentConstants.PDF_CONTENT_TYPE).stream(() -> new ByteArrayInputStream(
						DocumentUtils.convertTo(SaveFormat.PDF, DEFAULT_SAMPLE_FILE_PATH)))
				.build();
	}

	public StreamedContent convertPdfFile(String filePath) {
		WordFactory.loadLicense();
		content = DefaultStreamedContent.builder().name(CONVERTED_DEMO_FILE_NAME)
				.contentType(DocumentConstants.PDF_CONTENT_TYPE)
				.stream(() -> new ByteArrayInputStream(DocumentUtils.convertTo(SaveFormat.PDF, filePath))).build();
		return content;
	}

	public StreamedContent getContent() {
		return content;
	}

	public void setContent(StreamedContent content) {
		this.content = content;
	}

}
