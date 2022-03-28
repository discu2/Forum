package org.discu2.forum.common.exception;

import java.io.IOException;

public class IllegalFileException extends IOException {
    public IllegalFileException(String string) {
        super(string);
    }
}
