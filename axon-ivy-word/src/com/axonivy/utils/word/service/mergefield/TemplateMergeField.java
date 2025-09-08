package com.axonivy.utils.word.service.mergefield;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Locale;

import com.axonivy.utils.word.service.DocumentConstants;

public class TemplateMergeField {

  /** The MergeField Name */
  private String mergeFieldName;

  /** The MergeField value that should be inserted into the Merge field */
  private Object value;
  private Locale locale = DocumentConstants.DEFAULT_LOCALE;
  public TemplateMergeFieldType type = TemplateMergeFieldType.TEXT;
  public DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.YEAR_FIELD, locale);
  private NumberFormat numberFormat = NumberFormat.getInstance(locale);

  /**
   * Simple constructor with two parameters: the mergeFieldName and its value.
   */
  public TemplateMergeField(String mergeFieldName, Object value) {
    if (mergeFieldName == null || mergeFieldName.isBlank()) {
      throw new IllegalArgumentException("Merge field name cannot be blank");
    }
    if (value == null) {
      throw new IllegalArgumentException("Merge field value cannot be null");
    }
    this.mergeFieldName = mergeFieldName;
    this.value = value;
  }

  public String getMergeFieldName() {
    return mergeFieldName;
  }

  public void setMergeFieldName(String mergeFieldName) {
    this.mergeFieldName = mergeFieldName;
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  /**
   * Returns the value for mail merging (raw object, Aspose can handle numbers/strings).
   */
  public Object getValueForMailMerging() {
    return this.value;
  }

  @Override
  public String toString() {
    return mergeFieldName + " = " + value;
  }
  /**
   * Get the merge field value.
   * @return the mergeField Value as String. If the value is null, it returns an
   *         empty String. In case of Date or Number type, it returns the
   *         formatted String of the Date or Number. If the value is an object
   *         it uses the object toString() method.
   */
  public String getMergeFieldValue() {
    if (this.value == null) {
      return "";
    }
    try {
      if (this.type.is(TemplateMergeFieldType.DATE)) {
        return this.dateFormat.format(this.value);
      }
      if (this.type.is(TemplateMergeFieldType.NUMBER)) {
        return this.numberFormat.format(value);
      }
    } catch (IllegalArgumentException e) {

    }
    return this.value.toString();
  }
}