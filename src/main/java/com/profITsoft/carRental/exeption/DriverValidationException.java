package com.profITsoft.carRental.exeption;

public class DriverValidationException extends RuntimeException{
    public DriverValidationException() {
        super();
    }

    public DriverValidationException(String message) {
        super(message);
    }
}
