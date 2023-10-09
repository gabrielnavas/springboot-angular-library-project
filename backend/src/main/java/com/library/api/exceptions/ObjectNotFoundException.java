package com.library.api.exceptions;


public class ObjectNotFoundException extends RuntimeException {

    public ObjectNotFoundException(String objectName) {
        super(createMessage(objectName), new Exception(createMessage(objectName)));
    }

    public ObjectNotFoundException(String objectName, Throwable cause) {
        super(createMessage(objectName), cause);
    }

    public ObjectNotFoundException(Throwable cause) {
        super(createMessage(cause.getMessage()), cause);
    }


    private static String createMessage(String objectName) {
        return String.format("%s not found", objectName);
    }
}
