package com.axonivy.connector.word.demo.bean;

import java.io.ByteArrayInputStream;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.aspose.words.SaveFormat;
import com.axonivy.connector.word.WordFactory;
import com.axonivy.connector.word.demo.util.DocumentUtils;

@ManagedBean
@ViewScoped
public class ConvertDocumentBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private final String DEFAULT_SAMPLE_FILE_PATH = "/resources/demo.docx";

	public StreamedContent getConvertedFile() {
		WordFactory.loadLicense();
		return DefaultStreamedContent.builder().name("test.pdf").contentType("application/pdf").stream(
				() -> new ByteArrayInputStream(DocumentUtils.convertTo(SaveFormat.PDF, DEFAULT_SAMPLE_FILE_PATH)))
				.build();
	}
}
