package org.discu2.forum.common.packet;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountUpdateRequestPacket {

    private String nickname;
}
