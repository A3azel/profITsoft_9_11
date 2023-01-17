package com.profITsoft.carRental.exeption;

public class EntityValidationException extends RuntimeException{
    public EntityValidationException() {
        super();
    }

    public EntityValidationException(String message) {
        super(message);
    }

}
