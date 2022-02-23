package org.discu2.forum.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
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
    private List<String> likeUserIds;
    private List<String> dislikeUserIds;

    public TextBlock(String topicId, String ownerId, LocalDateTime postDataTime, LocalDateTime lastEditDataTime, String content, List<String> likeUserIds, List<String> dislikeUserIds) {
        this.topicId = topicId;
        this.ownerId = ownerId;
        this.postDataTime = postDataTime;
        this.lastEditDataTime = lastEditDataTime;
        this.content = content;
        this.likeUserIds = likeUserIds;
        this.dislikeUserIds = dislikeUserIds;
    }

    public static class Post extends TextBlock {

        @Getter
        @Setter
        private List<Comment> comments;

        public Post(String topicId, String ownerId, LocalDateTime postDataTime, LocalDateTime lastEditDataTime, String content, List<String> likeUserIds, List<String> dislikeUserIds, List<Comment> comments) {
            super(topicId, ownerId, postDataTime, lastEditDataTime, content, likeUserIds, dislikeUserIds);
            this.comments = comments;
        }
    }

    public static class Reply extends TextBlock {

        @Getter
        @Setter
        private List<Comment> comments;

        public Reply(String topicId, String ownerId, LocalDateTime postDataTime, LocalDateTime lastEditDataTime, String content, List<String> likeUserIds, List<String> dislikeUserIds, List<Comment> comments) {
            super(topicId, ownerId, postDataTime, lastEditDataTime, content, likeUserIds, dislikeUserIds);
            this.comments = comments;
        }
    }

    public static class Comment extends TextBlock {

        public Comment(String topicId, String ownerId, LocalDateTime postDataTime, LocalDateTime lastEditDataTime, String content, List<String> likeUserIds, List<String> dislikeUserIds) {
            super(topicId, ownerId, postDataTime, lastEditDataTime, content, likeUserIds, dislikeUserIds);
        }
    }
}
