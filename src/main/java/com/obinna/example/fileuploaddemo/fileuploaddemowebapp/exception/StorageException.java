package com.obinna.example.fileuploaddemo.fileuploaddemowebapp.exception;

public class StorageException extends RuntimeException {

    public StorageException(String errorMessage) {
        super(errorMessage);
    }

    public StorageException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

}
