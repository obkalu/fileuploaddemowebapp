package com.obinna.example.fileuploaddemo.fileuploaddemowebapp.exception;

public class StorageFileNotFoundException extends StorageException {

    public StorageFileNotFoundException(String errorMessage) {
        super(errorMessage);
    }

    public StorageFileNotFoundException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
}
