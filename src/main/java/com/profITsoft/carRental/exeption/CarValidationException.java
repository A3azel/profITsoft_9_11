package com.profITsoft.carRental.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class CarValidationException extends RuntimeException{
    public CarValidationException() {
        super();
    }

    public CarValidationException(String message) {
        super(message);
    }
}
