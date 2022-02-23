package org.discu2.forum.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
public class Board {

    @Id
    private String id;

    private String boardGroupId;
    private String ownerId;
    private List<String> cachedTopicIds;

    public Board(String boardGroupId, String ownerId, List<String> cachedTopicIds) {
        this.boardGroupId = boardGroupId;
        this.ownerId = ownerId;
        this.cachedTopicIds = cachedTopicIds;
    }
}
