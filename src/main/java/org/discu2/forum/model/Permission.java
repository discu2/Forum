package org.discu2.forum.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Permission {

    @Id
    private String id;

    private int level;
    private int color;

    public Permission(int level, int color) {
        this.level = level;
        this.color = color;
    }

}
