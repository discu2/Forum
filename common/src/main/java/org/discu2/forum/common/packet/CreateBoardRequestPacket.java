package org.discu2.forum.common.packet;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateBoardRequestPacket {

    private String groupName;
    private String name;
    private String[] basicPermissionRoles;
    private String[] moderatorRoles;
}
