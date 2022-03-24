package org.discu2.forum.api.packet;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class AccountPacket {
    private String username;
    private String nickname;
    private Set<String> roleIds;
}
