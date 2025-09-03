package com.axonivy.utils.axon.ivy.words.demo.bean;

import java.io.ByteArrayInputStream;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.file.UploadedFile;

import com.aspose.words.SaveFormat;
import com.axonivy.utils.axon.ivy.words.demo.util.DocumentUtils;
import com.axonivy.utils.axon.ivy.words.service.WordFactory;

@ManagedBean
@ViewScoped
public class ConvertDocumentBean implements Serializable {
  private static final long serialVersionUID = 1L;
  private final String PDF_EXTENTION = ".pdf";
  private final String DOT = ".";
  private UploadedFile uploadedFile;
  private DefaultStreamedContent convertedFile;

  public void convert() {
    if (uploadedFile != null) {
      String pdfFileName = updateFIleExtention();
      WordFactory.loadLicense();
      setConvertedFile(DefaultStreamedContent.builder().name(pdfFileName).contentType("application/pdf")
          .stream(() -> new ByteArrayInputStream(DocumentUtils.convertTo(SaveFormat.PDF, uploadedFile))).build());
    }
  }

  private String updateFIleExtention() {
    String originalName = uploadedFile.getFileName();
    String baseName = originalName != null && originalName.contains(DOT)
        ? originalName.substring(0, originalName.lastIndexOf(DOT))
        : originalName;
    String pdfFileName = baseName + PDF_EXTENTION;
    return pdfFileName;
  }

  public DefaultStreamedContent getConvertedFile() {
    return convertedFile;
  }

  public void setConvertedFile(DefaultStreamedContent convertedFile) {
    this.convertedFile = convertedFile;
  }

  public UploadedFile getUploadedFile() {
    return uploadedFile;
  }

  public void setUploadedFile(UploadedFile uploadedFile) {
    this.uploadedFile = uploadedFile;
  }
}