package org.discu2.forum.common.packet;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.discu2.forum.common.model.Avatar;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
public class AccountPacket {
    private String username;
    private String nickname;
    private Set<String> roleIds;
    private EnumMap<Avatar.Size, String> avatarIds;
}
