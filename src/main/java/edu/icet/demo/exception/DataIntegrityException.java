package edu.icet.demo.exception;

public class DataIntegrityException extends RuntimeException{

    public DataIntegrityException(String message) {
        super(message);
    }
}
