package com.axonivy.utils.axon.ivy.words.demo.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("ageValidator")
public class AgeValidator implements Validator {
  @Override
  public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
    if (value == null) {
      return;
    }
    int age;
    try {
      age = Integer.parseInt(value.toString());
    } catch (NumberFormatException e) {
      throw new ValidatorException(new FacesMessage("Age must be a number"));
    }

    if (age < 12 || age > 99) {
      throw new ValidatorException(new FacesMessage("Age must be between 12 and 99"));
    }
  }
}
