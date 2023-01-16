package com.profITsoft.carRental.validations;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Service
public class ErrorValidator {

    public String checkErrors(BindingResult bindingResult){
        StringBuilder errorMassage = new StringBuilder();
        if(bindingResult.hasErrors()){
            for(FieldError fieldError : bindingResult.getFieldErrors()){
                errorMassage.append(fieldError.getField()).append(" - ").append(fieldError.getDefaultMessage()).append("\n");
            }
            return errorMassage.toString();
        }
        return "";
    }
}
