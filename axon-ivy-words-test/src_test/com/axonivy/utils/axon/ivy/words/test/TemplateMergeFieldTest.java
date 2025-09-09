package com.axonivy.utils.axon.ivy.words.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.axonivy.utils.axon.ivy.words.service.mergefield.TemplateMergeField;
import com.axonivy.utils.axon.ivy.words.service.mergefield.TemplateMergeFieldType;

public class TemplateMergeFieldTest {

    private TemplateMergeField textField;

    @BeforeEach
    void setup() {
        textField = new TemplateMergeField("name", "John Doe");
    }

    @Test
    void constructor_shouldThrowException_whenNameIsNullOrBlank() {
        assertThatThrownBy(() -> new TemplateMergeField(null, "value"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("cannot be blank");

        assertThatThrownBy(() -> new TemplateMergeField("   ", "value"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void constructor_shouldThrowException_whenValueIsNull() {
        assertThatThrownBy(() -> new TemplateMergeField("name", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("cannot be null");
    }

    @Test
    void constructor_shouldCreateField_whenValidInputs() {
        TemplateMergeField field = new TemplateMergeField("age", 30);
        assertThat(field.getMergeFieldName()).isEqualTo("age");
        assertThat(field.getValue()).isEqualTo(30);
    }

    @Test
    void gettersAndSetters_shouldWork() {
        textField.setMergeFieldName("fullName");
        textField.setValue("Jane Doe");

        assertThat(textField.getMergeFieldName()).isEqualTo("fullName");
        assertThat(textField.getValue()).isEqualTo("Jane Doe");
    }

    @Test
    void getValueForMailMerging_shouldReturnOriginalValue() {
        assertThat(textField.getValueForMailMerging()).isEqualTo("John Doe");
    }

    @Test
    void getMergeFieldValue_shouldReturnEmptyString_whenValueIsNull() {
        TemplateMergeField field = new TemplateMergeField("test", "dummy");
        field.setValue(null);

        assertThat(field.getMergeFieldValue()).isEqualTo("");
    }

    @Test
    void getMergeFieldValue_shouldReturnFormattedDate_whenTypeIsDate() {
        Date now = new Date();
        TemplateMergeField field = new TemplateMergeField("birthdate", now);
        field.type = TemplateMergeFieldType.DATE;
        field.dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String formatted = field.getMergeFieldValue();
        assertThat(formatted).matches("\\d{4}-\\d{2}-\\d{2}");
    }

    @Test
    void getMergeFieldValue_shouldReturnFormattedNumber_whenTypeIsNumber() {
        TemplateMergeField field = new TemplateMergeField("salary", 12345.67);
        field.type = TemplateMergeFieldType.NUMBER;

        String formatted = field.getMergeFieldValue();
        assertThat(formatted).contains("12"); 
    }

    @Test
    void getMergeFieldValue_shouldFallbackToToString_whenTypeIsText() {
        TemplateMergeField field = new TemplateMergeField("field", 99);
        field.type = TemplateMergeFieldType.TEXT;

        assertThat(field.getMergeFieldValue()).isEqualTo("99");
    }

    @Test
    void toString_shouldIncludeFieldNameAndValue() {
        assertThat(textField.toString()).isEqualTo("name = John Doe");
    }
}
