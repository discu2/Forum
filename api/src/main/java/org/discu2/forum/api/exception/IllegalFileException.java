package org.discu2.forum.api.exception;

import java.io.IOException;

public class IllegalFileException extends IOException {
    public IllegalFileException(String string) {
        super(string);
    }
}
