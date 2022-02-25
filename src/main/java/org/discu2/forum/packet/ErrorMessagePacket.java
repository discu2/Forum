package org.discu2.forum.packet;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorMessagePacket {

    private String errorMessage;

}
