package org.discu2.forum.service;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.discu2.forum.exception.DataNotFoundException;
import org.discu2.forum.model.TextBlock;
import org.discu2.forum.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final ReplyService replyService;

    public TextBlock.Comment createNewComment(@NonNull TextBlock.Comment.MasterType masterType, @NonNull String masterId, @NonNull String accountId, @NonNull String content) throws DataNotFoundException {

        // Make sure mater is existed
        switch (masterType) {
            case POST -> postService.loadById(masterId);
            case REPLY -> replyService.loadById(masterId);
        }

        var now = LocalDateTime.now();
        var post = new TextBlock.Comment(
                null,
                masterType,
                masterId,
                accountId,
                now,
                now,
                content,
                Lists.newArrayList(),
                Lists.newArrayList());

        return commentRepository.save(post);
    }

    public List<TextBlock.Comment> loadCommentsByMasterId(@NonNull TextBlock.Comment.MasterType masterType, @NonNull String masterId) throws DataNotFoundException {

        var comments = commentRepository.findByMasterTypeAndMasterId(masterType.name(), masterId);

        if (comments.isEmpty()) throw new DataNotFoundException(TextBlock.Comment.class, "masterType:masterId", masterType.name() + ":" + masterId);

        return comments;
    }
}
