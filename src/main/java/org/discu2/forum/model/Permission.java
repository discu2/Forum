package org.discu2.forum.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
public class Permission {

    @Id
    private String id;

    @Indexed(unique = true)
    private String permission;

    public Permission(String permission) {
        this.permission = permission;
    }
}
