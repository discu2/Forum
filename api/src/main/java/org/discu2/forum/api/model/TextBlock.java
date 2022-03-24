package org.discu2.forum.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
public abstract class TextBlock {

    @Id
    private String id;

    private String ownerUsername;

    private Long postTime;
    private Long lastEditTime;
    private String content;
    private List<String> likeUsers;
    private List<String> dislikeUsers;


    @Document
    @CompoundIndex(name = "post_time_asc", def = "{ 'postTime': 1 }")
    public static class Post extends TextBlock {

        @Getter
        @Setter
        @Indexed(unique = true)
        private String topicId;

        @Getter
        @Setter
        private Boolean originPost;

        public Post(String id, String topicId, String ownerUsername, Long postTime, Long lastEditTime, String content, Boolean originPost, List<String> likeUsers, List<String> dislikeUsers) {
            super(id, ownerUsername, postTime, lastEditTime, content, likeUsers, dislikeUsers);
            this.originPost = originPost;
            this.topicId = topicId;
        }
    }

    @Document
    @CompoundIndex(name = "post_time_asc", def = "{ 'postTime': 1 }")
    public static class Comment extends TextBlock {

        @Getter
        @Setter
        private String postId;

        public Comment(String id, String postId, String ownerUsername, Long postTime, Long lastEditTime, String content, List<String> likeUsers, List<String> dislikeUsers) {
            super(id, ownerUsername, postTime, lastEditTime, content, likeUsers, dislikeUsers);
            this.postId = postId;
        }
    }
}
