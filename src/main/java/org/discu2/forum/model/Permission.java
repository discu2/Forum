package org.discu2.forum.model;

import org.springframework.data.annotation.Id;

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
