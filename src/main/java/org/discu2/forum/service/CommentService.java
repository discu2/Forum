package org.discu2.forum.service;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.discu2.forum.exception.DataNotFoundException;
import org.discu2.forum.model.Account;
import org.discu2.forum.model.TextBlock;
import org.discu2.forum.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final AccountService accountService;
    private final PostService postService;

    public TextBlock.Comment createNewComment(@NonNull String postId,
                                              @NonNull String username,
                                              @NonNull String content) throws DataNotFoundException {

        // Make sure mater is existed
        postService.loadById(postId);

        var accountId = ((Account) accountService.loadUserByUsername(username)).getId();
        var now = new Date().getTime();

        var post = new TextBlock.Comment(
                null,
                postId,
                accountId,
                username,
                now,
                now,
                content,
                Lists.newArrayList(),
                Lists.newArrayList());

        return commentRepository.save(post);
    }

    public List<TextBlock.Comment> loadCommentsByPostId(@NonNull String postId) throws DataNotFoundException {

        var comments = commentRepository.findByPostId(postId);

        if (comments.isEmpty()) throw new DataNotFoundException(TextBlock.Comment.class, "postId", postId);

        return comments;
    }
}
