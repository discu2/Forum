package org.discu2.forum.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Topic {

    @Id
    private String id;

    private String boardId;
    private String postId;
    private String title;
    private boolean pinned;
    private int pinnedOrder;
    private LocalDateTime createDataTime;
    private List<String> cachedReplyIds;

    public Topic(String boardId, String postId, String title, boolean pinned, int pinnedOrder, LocalDateTime createDataTime, List<String> cachedReplyIds) {
        this.boardId = boardId;
        this.postId = postId;
        this.title = title;
        this.pinned = pinned;
        this.pinnedOrder = pinnedOrder;
        this.createDataTime = createDataTime;
        this.cachedReplyIds = cachedReplyIds;
    }
}
