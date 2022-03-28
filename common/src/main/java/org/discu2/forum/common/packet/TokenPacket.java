package org.discu2.forum.common.packet;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenPacket{

    private String accessToken;
    private String refreshToken;
}
