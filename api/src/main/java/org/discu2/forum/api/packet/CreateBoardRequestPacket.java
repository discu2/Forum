package org.discu2.forum.api.packet;

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
