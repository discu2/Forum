package org.discu2.forum.api.model;

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
    private String ownerUsername;
    private String title;
    private String lastPosterUsername;

    private Long createTime;
    private Long lastPostTime;

    private int pinnedOrder;

}
