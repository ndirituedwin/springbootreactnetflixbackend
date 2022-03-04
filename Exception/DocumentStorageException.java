package com.ndirituedwin.Exception;

public class DocumentStorageException extends Throwable {
    public DocumentStorageException(String s) {
        super(s);
    }
    public DocumentStorageException(String message,Throwable cause){
        super(message,cause);
    }
}
