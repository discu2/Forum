package org.discu2.forum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public abstract class TextBlock {

    @Id
    private String id;

    private String topicId;
    private String ownerId;
    private LocalDateTime postDataTime;
    private LocalDateTime lastEditDataTime;
    private String content;
    private List<String> likeUserIds;
    private List<String> dislikeUserIds;

    @Document
    public static class Post extends TextBlock {

        @Getter
        @Setter
        private List<Comment> comments;

        public Post(String id, String topicId, String ownerId, LocalDateTime postDataTime, LocalDateTime lastEditDataTime, String content, List<String> likeUserIds, List<String> dislikeUserIds, List<Comment> comments) {
            super(id, topicId, ownerId, postDataTime, lastEditDataTime, content, likeUserIds, dislikeUserIds);
            this.comments = comments;
        }
    }

    @Document
    public static class Reply extends TextBlock {

        @Getter
        @Setter
        private List<Comment> comments;

        public Reply(String id, String topicId, String ownerId, LocalDateTime postDataTime, LocalDateTime lastEditDataTime, String content, List<String> likeUserIds, List<String> dislikeUserIds, List<Comment> comments) {
            super(id, topicId, ownerId, postDataTime, lastEditDataTime, content, likeUserIds, dislikeUserIds);
            this.comments = comments;
        }
    }

    @Document
    public static class Comment extends TextBlock {

        public Comment(String id, String topicId, String ownerId, LocalDateTime postDataTime, LocalDateTime lastEditDataTime, String content, List<String> likeUserIds, List<String> dislikeUserIds) {
            super(id, topicId, ownerId, postDataTime, lastEditDataTime, content, likeUserIds, dislikeUserIds);
        }
    }
}
