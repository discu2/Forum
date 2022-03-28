package org.discu2.forum.core.service;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.discu2.forum.common.model.TextBlock;
import org.discu2.forum.common.exception.DataNotFoundException;
import org.discu2.forum.core.repository.CommentRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final MongoTemplate mongoTemplate;


    public TextBlock.Comment createNewComment(@NonNull String postId,
                                              @NonNull String username,
                                              @NonNull String content) throws DataNotFoundException {
        
        // Make sure master is existed
        postService.loadById(postId);

        var now = new Date().getTime();

        var post = new TextBlock.Comment(
                null,
                postId,
                username,
                now,
                now,
                content,
                Lists.newArrayList(),
                Lists.newArrayList());

        return commentRepository.save(post);
    }

    public List<TextBlock.Comment> loadCommentsByPostId(@NonNull String postId, int page, int pageSize) throws DataNotFoundException {

        var query = Query.query(Criteria.where("postId").is(postId))
                //.with(Sort.by("postTime").ascending())
                .withHint("post_time_asc")
                .skip((page - 1) * pageSize).
                limit(pageSize);

        return mongoTemplate.find(query, TextBlock.Comment.class);
    }
}
