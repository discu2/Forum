package org.discu2.forum.packet;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ErrorMessagePacket {

    private HttpStatus status;
    private String message;

}
