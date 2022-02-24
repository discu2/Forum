package org.discu2.forum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Document
public class Topic {

    @Id
    private String id;

    private String boardId;
    private String posterId;
    private String title;
    private boolean pinned;
    private int pinnedOrder;
    private LocalDateTime createDateTime;

}
