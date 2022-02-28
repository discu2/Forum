package org.discu2.forum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public abstract class TextBlock {

    @Id
    private String id;

    private String accountId;
    private LocalDateTime postDateTime;
    private LocalDateTime lastEditDateTime;
    private String content;
    private List<String> likeUserIds;
    private List<String> dislikeUserIds;


    @Document
    public static class Post extends TextBlock {

        @Getter
        @Setter
        @Indexed(unique = true)
        private String topicId;

        public Post(String id, String topicId, String ownerId, LocalDateTime postDateTime, LocalDateTime lastEditDateTime, String content, List<String> likeUserIds, List<String> dislikeUserIds) {
            super(id, ownerId, postDateTime, lastEditDateTime, content, likeUserIds, dislikeUserIds);
            this.topicId = topicId;
        }
    }

    @Document
    public static class Reply extends TextBlock {

        @Getter
        @Setter
        private String topicId;
        public Reply(String id, String topicId, String ownerId, LocalDateTime postDateTime, LocalDateTime lastEditDateTime, String content, List<String> likeUserIds, List<String> dislikeUserIds) {
            super(id, ownerId, postDateTime, lastEditDateTime, content, likeUserIds, dislikeUserIds);
            this.topicId = topicId;
        }
    }

    @Document
    public static class Comment extends TextBlock {

        public enum MasterType {
            POST, REPLY
        }

        @Getter
        @Setter
        private String masterType;

        @Getter
        @Setter
        private String masterId;

        public Comment(String id, MasterType masterType, String masterId, String ownerId, LocalDateTime postDateTime, LocalDateTime lastEditDateTime, String content, List<String> likeUserIds, List<String> dislikeUserIds) {
            super(id, ownerId, postDateTime, lastEditDateTime, content, likeUserIds, dislikeUserIds);
            this.masterType = masterType.name();
            this.masterId = masterId;
        }
    }
}
