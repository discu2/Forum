package org.discu2.forum.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.discu2.forum.common.exception.BadPacketFormatException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class JsonConverter {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static void PacketToJsonResponse(HttpServletResponse response, Object packet) throws IOException {
        response.setContentType(APPLICATION_JSON_VALUE);
        MAPPER.writeValue(response.getOutputStream(), packet);
    }

    public static <T> T requestToPacket(InputStream stream, Class<T> packet) throws BadPacketFormatException {

        try {
            return MAPPER.readValue(stream, packet);
        } catch (IOException e) {
            throw new BadPacketFormatException(e.getMessage());
        }
    }
}
