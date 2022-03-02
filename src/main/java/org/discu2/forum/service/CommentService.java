package org.discu2.forum.service;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.discu2.forum.exception.DataNotFoundException;
import org.discu2.forum.model.Account;
import org.discu2.forum.model.TextBlock;
import org.discu2.forum.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final AccountService accountService;
    private final PostService postService;

    public TextBlock.Comment createNewComment(@NonNull String masterId,
                                              @NonNull String username,
                                              @NonNull String content) throws DataNotFoundException {

        // Make sure mater is existed
        postService.loadById(masterId);

        var accountId = ((Account) accountService.loadUserByUsername(username)).getId();
        var now = new Date().getTime();

        var post = new TextBlock.Comment(
                null,
                masterId,
                accountId,
                username,
                now,
                now,
                content,
                Lists.newArrayList(),
                Lists.newArrayList());

        return commentRepository.save(post);
    }

    public List<TextBlock.Comment> loadCommentsByMasterId(@NonNull String masterId) throws DataNotFoundException {

        var comments = commentRepository.findByMasterId(masterId);

        if (comments.isEmpty()) throw new DataNotFoundException(TextBlock.Comment.class, "masterId", masterId);

        return comments;
    }
}
