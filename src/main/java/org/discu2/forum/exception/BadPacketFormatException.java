package org.discu2.forum.exception;

import java.io.IOException;

public class BadPacketFormatException extends IOException {

    public BadPacketFormatException(String string) {
        super(string);
    }
}
