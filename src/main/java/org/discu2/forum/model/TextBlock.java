package org.discu2.forum.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;

@Data
public abstract class TextBlock {

    @Id
    private String id;

    private String topicId;
    private String ownerId;
    private LocalDateTime postDataTime;
    private LocalDateTime lastEditDataTime;
    private String content;
    private List<String> comments;

    public TextBlock(String topicId, String ownerId, LocalDateTime postDataTime, LocalDateTime lastEditDataTime, String content, List<String> comments) {
        this.topicId = topicId;
        this.ownerId = ownerId;
        this.postDataTime = postDataTime;
        this.lastEditDataTime = lastEditDataTime;
        this.content = content;
        this.comments = comments;
    }

    public class Post extends TextBlock {

        public Post(String topicId, String ownerId, LocalDateTime postDataTime, LocalDateTime lastEditDataTime, String content, List<String> comments) {
            super(topicId, ownerId, postDataTime, lastEditDataTime, content, comments);
        }
    }

    public class Reply extends TextBlock {

        public Reply(String topicId, String ownerId, LocalDateTime postDataTime, LocalDateTime lastEditDataTime, String content, List<String> comments) {
            super(topicId, ownerId, postDataTime, lastEditDataTime, content, comments);
        }
    }
}
