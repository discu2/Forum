package org.discu2.forum.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
public class Group {

    @Id
    private String id;

    private String name;
    private List<Permission> permissions;

    public Group(String name, List<Permission> permissions) {
        this.name = name;
        this.permissions = permissions;
    }
}
