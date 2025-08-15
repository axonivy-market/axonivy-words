package com.axonivy.connector.word.demo.bean;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;

import com.aspose.words.DocSaveOptions;
import com.aspose.words.Document;
import com.aspose.words.SaveFormat;
import com.aspose.words.SaveOptions;

import ch.ivyteam.ivy.environment.Ivy;

@ManagedBean
@ViewScoped
public class ConvertDocumentBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private UploadedFile file;

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}
	
	public StreamedContent getConvertAndGetFile() {
		return DefaultStreamedContent.builder().name(file.getFileName().replaceAll("\\.(docx?|DOCX?)$", ".pdf"))
				.contentType("application/pdf").stream(() -> new ByteArrayInputStream(convertTo(SaveFormat.PDF, file)))
				.build();
	}

	public byte[] convertTo(int saveFormat, UploadedFile inputFile) {
		try (InputStream inputStream = file.getInputStream();
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			Document doc = new Document(inputStream);
			SaveOptions options = DocSaveOptions.createSaveOptions(saveFormat);
			doc.save(outputStream, options);
			Ivy.log().warn("finish" + outputStream.size());
			return outputStream.toByteArray();
		} catch (Exception e) {
			Ivy.log().warn("oh no");
			return new byte[0];
		}
	}
}
