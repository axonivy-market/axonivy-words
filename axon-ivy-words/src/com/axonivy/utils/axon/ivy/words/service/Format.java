package com.axonivy.utils.axon.ivy.word.service;

import com.aspose.words.SaveFormat;

/**
 * Enumeration of supported document formats for conversion.
 * Maps to Aspose.Words SaveFormat constants.
 */
public enum Format {
  PDF(SaveFormat.PDF),
  DOCX(SaveFormat.DOCX),
  DOC(SaveFormat.DOC),
  RTF(SaveFormat.RTF),
  TXT(SaveFormat.TEXT),
  HTML(SaveFormat.HTML),
  EPUB(SaveFormat.EPUB),
  ODT(SaveFormat.ODT),
  XPS(SaveFormat.XPS);

  private final int asposeFormat;

  Format(int asposeFormat) {
    this.asposeFormat = asposeFormat;
  }

  public int getAsposeFormat() {
    return asposeFormat;
  }
}
