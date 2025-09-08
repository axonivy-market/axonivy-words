package com.axonivy.utils.axon.ivy.word.demo.examples;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import com.axonivy.utils.axon.ivy.word.service.Format;
import com.axonivy.utils.axon.ivy.word.service.WordFactory;

/**
 * Comprehensive examples demonstrating the new fluent API for document conversion.
 * 
 * The WordFactory class now supports a fluent API pattern for document conversion:
 * 
 * Basic Pattern: WordFactory.convert().from(source).to(format).asOutput()
 * 
 * Examples:
 * - WordFactory.convert().from(file).toPdf().asBytes()
 * - WordFactory.convert().from(file).to(Format.PDF).asFile(outputPath)
 * - WordFactory.convert().from(inputStream).to(Format.DOCX).asInputStream()
 */
public final class FluentApiExamples {
  
  private FluentApiExamples() {
    // Utility class
  }

  /**
   * Example 1: Convert from File to PDF as byte array
   * This is the most common use case for web applications.
   */
  public static byte[] convertFileToPdfBytes(File inputFile) {
    return WordFactory.convert()
        .from(inputFile)
        .toPdf()
        .asBytes();
  }

  /**
   * Example 2: Convert from InputStream to specific format as file
   * Useful when you have the document content as a stream.
   */
  public static File convertStreamToFile(InputStream inputStream, Format targetFormat, String outputPath) {
    return WordFactory.convert()
        .from(inputStream)
        .to(targetFormat)
        .asFile(outputPath);
  }

  /**
   * Example 3: Convert from file path to PDF and save as file
   * Direct file-to-file conversion.
   */
  public static File convertFilePathToPdfFile(String inputPath, String outputPath) {
    return WordFactory.convert()
        .from(inputPath)
        .toPdf()
        .asFile(outputPath);
  }

  /**
   * Example 4: Convert from byte array to different formats
   * Useful when working with document data in memory.
   */
  public static byte[] convertBytesToFormat(byte[] documentBytes, Format targetFormat) {
    return WordFactory.convert()
        .from(documentBytes)
        .to(targetFormat)
        .asBytes();
  }

  /**
   * Example 5: Convert to InputStream for streaming responses
   * Perfect for web responses where you want to stream the converted document.
   */
  public static InputStream convertToInputStream(File inputFile, Format targetFormat) {
    return WordFactory.convert()
        .from(inputFile)
        .to(targetFormat)
        .asInputStream();
  }

  /**
   * Example 6: Batch conversion demonstration
   * Shows how to convert multiple files efficiently.
   */
  public static void batchConversion(File[] inputFiles, String outputDirectory) {
    for (File inputFile : inputFiles) {
      String outputPath = outputDirectory + "/" + 
          getFileNameWithoutExtension(inputFile) + ".pdf";
      
      WordFactory.convert()
          .from(inputFile)
          .toPdf()
          .asFile(outputPath);
    }
  }

  /**
   * Example 7: Convert with different output formats
   * Demonstrates various supported formats.
   */
  public static void demonstrateFormats(File inputFile, String outputDir) {
    String baseName = getFileNameWithoutExtension(inputFile);
    
    // Convert to PDF
    WordFactory.convert().from(inputFile).to(Format.PDF)
        .asFile(outputDir + "/" + baseName + ".pdf");
    
    // Convert to DOCX
    WordFactory.convert().from(inputFile).to(Format.DOCX)
        .asFile(outputDir + "/" + baseName + ".docx");
    
    // Convert to HTML
    WordFactory.convert().from(inputFile).to(Format.HTML)
        .asFile(outputDir + "/" + baseName + ".html");
    
    // Convert to RTF
    WordFactory.convert().from(inputFile).to(Format.RTF)
        .asFile(outputDir + "/" + baseName + ".rtf");
  }

  /**
   * Example 8: Working with try-with-resources for InputStreams
   * Best practice when working with InputStreams.
   */
  public static byte[] safeStreamConversion(File inputFile) {
    try (FileInputStream fis = new FileInputStream(inputFile)) {
      return WordFactory.convert()
          .from(fis)
          .toPdf()
          .asBytes();
    } catch (Exception e) {
      throw new RuntimeException("Failed to convert file: " + inputFile.getName(), e);
    }
  }

  /**
   * Utility method to get filename without extension.
   */
  private static String getFileNameWithoutExtension(File file) {
    String name = file.getName();
    int lastDot = name.lastIndexOf('.');
    return lastDot > 0 ? name.substring(0, lastDot) : name;
  }

  /**
   * Example 9: Error handling demonstration
   * Shows how to handle conversion errors gracefully.
   */
  public static byte[] convertWithErrorHandling(File inputFile) {
    try {
      return WordFactory.convert()
          .from(inputFile)
          .toPdf()
          .asBytes();
    } catch (Exception e) {
      // Log the error and return empty array or handle as appropriate
      System.err.println("Conversion failed for file: " + inputFile.getName() + ", error: " + e.getMessage());
      return new byte[0];
    }
  }

  /**
   * Example 10: Chaining multiple conversions
   * Shows how to work with the fluent API in complex scenarios.
   */
  public static void chainedConversions(File inputFile, String tempDir, String finalOutputPath) {
    // First convert to DOCX (as intermediate format)
    File tempDocx = WordFactory.convert()
        .from(inputFile)
        .to(Format.DOCX)
        .asFile(tempDir + "/temp.docx");
    
    // Then convert the DOCX to PDF
    WordFactory.convert()
        .from(tempDocx)
        .toPdf()
        .asFile(finalOutputPath);
    
    // Clean up temporary file
    tempDocx.delete();
  }
}
