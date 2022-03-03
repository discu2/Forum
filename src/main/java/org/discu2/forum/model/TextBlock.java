package org.discu2.forum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
public abstract class TextBlock {

    @Id
    private String id;

    private String ownerId;
    private String username;

    private Long postTime;
    private Long lastEditTime;
    private String content;
    private List<String> likeUserIds;
    private List<String> dislikeUserIds;


    @Document
    public static class Post extends TextBlock {

        @Getter
        @Setter
        @Indexed(unique = true)
        private String topicId;

        @Getter
        @Setter
        private Boolean originPost;

        public Post(String id, String topicId, String ownerId, String username, Long postTime, Long lastEditTime, String content, Boolean originPost, List<String> likeUserIds, List<String> dislikeUserIds) {
            super(id, ownerId, username, postTime, lastEditTime, content, likeUserIds, dislikeUserIds);
            this.originPost = originPost;
            this.topicId = topicId;
        }
    }

    @Document
    public static class Comment extends TextBlock {

        @Getter
        @Setter
        private String postId;

        public Comment(String id, String postId, String ownerId, String username, Long postTime, Long lastEditTime, String content, List<String> likeUserIds, List<String> dislikeUserIds) {
            super(id, ownerId, username, postTime, lastEditTime, content, likeUserIds, dislikeUserIds);
            this.postId = postId;
        }
    }
}
