package org.discu2.forum.packet;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class RegisterRequestPacket extends LoginRequestPacket {

    @Getter
    @Setter
    private String mail;

}
