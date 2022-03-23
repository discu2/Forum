package org.discu2.forum.exception;

import java.io.IOException;

public class AlreadyExistException extends IOException {

    public AlreadyExistException(Class _class) {
        super(_class.getSimpleName() + " already exist");
    }
}
