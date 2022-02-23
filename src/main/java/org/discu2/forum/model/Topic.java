package org.discu2.forum.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
public class Topic {

    @Id
    private String id;

    private String boardId;
    private String posterId;
    private String title;
    private boolean pinned;
    private int pinnedOrder;
    private LocalDateTime createDateTime;

    public Topic(String boardId, String posterId, String title, boolean pinned, int pinnedOrder, LocalDateTime createDateTime) {
        this.boardId = boardId;
        this.posterId = posterId;
        this.title = title;
        this.pinned = pinned;
        this.pinnedOrder = pinnedOrder;
        this.createDateTime = createDateTime;
    }
}
