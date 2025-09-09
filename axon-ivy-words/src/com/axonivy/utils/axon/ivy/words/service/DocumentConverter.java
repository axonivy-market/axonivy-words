package com.axonivy.utils.axon.ivy.words.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

import com.aspose.words.DocSaveOptions;
import com.aspose.words.Document;
import com.aspose.words.SaveFormat;
import com.aspose.words.SaveOptions;

import ch.ivyteam.ivy.environment.Ivy;

/**
 * Fluent API for document conversion operations. Provides a chain of methods to convert documents from one format to
 * another.
 */
public class DocumentConverter {
  private Document document;
  private Integer targetFormat;

  /**
   * Creates a new DocumentConverter instance. Package-private constructor to ensure creation only through WordFactory.
   */
  DocumentConverter() {}

  /**
   * Sets the source document from an InputStream.
   * 
   * @param inputStream the input stream containing the document data
   * @return this converter instance for method chaining
   * @throws DocumentConversionException if document loading fails
   */
  public DocumentConverter from(InputStream inputStream) {
    try {
      this.document = new Document(inputStream);
      return this;
    } catch (Exception e) {
      Ivy.log().error("Failed to load document from InputStream", e);
      throw new DocumentConversionException("Failed to load document", e);
    }
  }

  /**
   * Sets the source document from a File.
   * 
   * @param file the file containing the document
   * @return this converter instance for method chaining
   * @throws DocumentConversionException if document loading fails
   */
  public DocumentConverter from(File file) {
    try {
      this.document = new Document(file.getAbsolutePath());
      return this;
    } catch (Exception e) {
      Ivy.log().error("Failed to load document from file: " + file.getAbsolutePath(), e);
      throw new DocumentConversionException("Failed to load document from file", e);
    }
  }

  /**
   * Sets the source document from a file path.
   * 
   * @param filePath the path to the file containing the document
   * @return this converter instance for method chaining
   * @throws DocumentConversionException if document loading fails
   */
  public DocumentConverter from(String filePath) {
    try {
      this.document = new Document(filePath);
      return this;
    } catch (Exception e) {
      Ivy.log().error("Failed to load document from path: " + filePath, e);
      throw new DocumentConversionException("Failed to load document from path", e);
    }
  }

  /**
   * Sets the source document from a byte array.
   * 
   * @param bytes the byte array containing the document data
   * @return this converter instance for method chaining
   * @throws DocumentConversionException if document loading fails
   */
  public DocumentConverter from(byte[] bytes) {
    try {
      this.document = new Document(new java.io.ByteArrayInputStream(bytes));
      return this;
    } catch (Exception e) {
      Ivy.log().error("Failed to load document from byte array", e);
      throw new DocumentConversionException("Failed to load document from byte array", e);
    }
  }

  /**
   * Converts the document to PDF format.
   * 
   * @return this converter instance for method chaining
   */
  public DocumentConverter toPdf() {
    return to(SaveFormat.PDF);
  }

  /**
   * Converts the document to the specified format.
   * 
   * @param format the target format
   * @return this converter instance for method chaining
   */
  public DocumentConverter to(int format) {
    if (document == null) {
      throw new IllegalStateException("No source document set. Call from() method first.");
    }
    this.targetFormat = format;
    return this;
  }

  /**
   * Converts the document and returns the result as a byte array.
   * 
   * @return the converted document as byte array
   * @throws DocumentConversionException if conversion fails
   */
  public byte[] asBytes() {
    validateConversionReady();
    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
      SaveOptions options = DocSaveOptions.createSaveOptions(targetFormat);
      document.save(outputStream, options);
      return outputStream.toByteArray();
    } catch (Exception e) {
      Ivy.log().error("Failed to convert document", e);
      throw new DocumentConversionException("Failed to convert document", e);
    }
  }

  /**
   * Converts the document and saves it as a file.
   * 
   * @param outputPath the path where the converted file should be saved
   * @return the File object representing the saved file
   * @throws DocumentConversionException if conversion or file saving fails
   */
  public File asFile(String outputPath) {
    validateConversionReady();
    try {
      File outputFile = new File(outputPath);
      // Ensure parent directories exist
      File parentDir = outputFile.getParentFile();
      if (parentDir != null && !parentDir.exists()) {
        parentDir.mkdirs();
      }

      SaveOptions options = DocSaveOptions.createSaveOptions(targetFormat);
      document.save(outputPath, options);
      return outputFile;
    } catch (Exception e) {
      Ivy.log().error("Failed to save converted document to: " + outputPath, e);
      throw new DocumentConversionException("Failed to save converted document", e);
    }
  }

  /**
   * Converts the document and saves it as a file.
   * 
   * @param outputFile the File object where the converted document should be saved
   * @return the File object representing the saved file
   * @throws DocumentConversionException if conversion or file saving fails
   */
  public File asFile(File outputFile) {
    return asFile(outputFile.getAbsolutePath());
  }

  /**
   * Converts the document and returns it as an InputStream. Note: The caller is responsible for closing the returned
   * InputStream.
   * 
   * @return an InputStream containing the converted document data
   * @throws DocumentConversionException if conversion fails
   */
  public InputStream asInputStream() {
    byte[] bytes = asBytes();
    return new ByteArrayInputStream(bytes);
  }

  /**
   * Validates that the converter is ready for conversion.
   * 
   * @throws IllegalStateException if document or target format is not set
   */
  private void validateConversionReady() {
    if (document == null) {
      throw new IllegalStateException("No source document set. Call from() method first.");
    }
    if (targetFormat == null) {
      throw new IllegalStateException("No target format set. Call to() or toPdf() method first.");
    }
  }
}
