package org.discu2.forum.api.packet;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountUpdateRequestPacket {

    private String nickname;
}
