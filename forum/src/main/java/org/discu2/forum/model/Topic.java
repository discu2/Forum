package org.discu2.forum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Document
@CompoundIndex(name = "default_desc", def = "{ 'lastPostTime': -1, 'pinnedOrder': -1 }")
public class Topic {

    @Id
    private String id;

    private String boardId;
    private String ownerId;
    private String username;
    private String title;
    private String lastPosterId;
    private String lastPosterUsername;

    private Long createTime;
    private Long lastPostTime;

    private int pinnedOrder;

}
