package com.profITsoft.carRental.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class DriverValidationException extends RuntimeException{
    public DriverValidationException() {
        super();
    }

    public DriverValidationException(String message) {
        super(message);
    }
}
