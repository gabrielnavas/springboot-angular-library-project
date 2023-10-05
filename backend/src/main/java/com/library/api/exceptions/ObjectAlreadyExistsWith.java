package com.library.api.exceptions;

public class ObjectAlreadyExistsWith extends RuntimeException {

    public ObjectAlreadyExistsWith(String objectName, String attribute) {
        super(createMessage(objectName, attribute), new Exception(createMessage(objectName, attribute)));
    }

    public ObjectAlreadyExistsWith(String objectName, String attribute, Throwable cause) {
        super(createMessage(objectName, attribute), cause);
    }

    private static String createMessage(String objectName, String attribute) {
        return String.format("object %s already exists with attribute %s", objectName, attribute);
    }
}
