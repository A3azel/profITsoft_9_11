package com.profITsoft.carRental.exeption;

public class CarValidationException extends RuntimeException{
    public CarValidationException() {
        super();
    }

    public CarValidationException(String message) {
        super(message);
    }

}
