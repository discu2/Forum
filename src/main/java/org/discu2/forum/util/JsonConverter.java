package org.discu2.forum.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.discu2.forum.exception.BadPacketFormatException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class JsonConverter {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static void PacketToJsonResponse(OutputStream stream, Object packet) throws IOException {
        MAPPER.writeValue(stream, packet);
    }

    public static <T> T requestToPacket(InputStream stream, Class<T> packet) throws BadPacketFormatException {

        try {
            return MAPPER.readValue(stream, packet);
        } catch (IOException e) {
            throw new BadPacketFormatException(e.getMessage());
        }
    }
}
