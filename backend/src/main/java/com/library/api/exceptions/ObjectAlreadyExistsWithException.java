package com.library.api.exceptions;

public class ObjectAlreadyExistsWithException extends RuntimeException {

    public ObjectAlreadyExistsWithException(String objectName, String attribute, String value) {
        super(createMessage(objectName, attribute, value), new Exception(createMessage(objectName, attribute, value)));
    }

    public ObjectAlreadyExistsWithException(String objectName, String attribute, String value, Throwable cause) {
        super(createMessage(objectName, attribute, value), cause);
    }

    private static String createMessage(String objectName, String attribute, String value) {
        return String.format("%s already exists with attribute %s with value %s", objectName, attribute, value);
    }
}
