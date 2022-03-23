package org.discu2.forum.packet;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginRequestPacket {

    private String username;
    private String password;
}
