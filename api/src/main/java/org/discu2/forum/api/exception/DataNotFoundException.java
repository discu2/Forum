package org.discu2.forum.api.exception;

import java.io.IOException;

public class DataNotFoundException extends IOException {

    public DataNotFoundException(String className, String keyType, String key) {
        super("No "+ className + " is found by [" + keyType + ":" + key + "]");
    }

    public DataNotFoundException(Class _class, String keyType, String key) {
        this(_class.getSimpleName(), keyType, key);
    }
}
