package org.discu2.forum.service;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.discu2.forum.exception.DataNotFoundException;
import org.discu2.forum.model.TextBlock;
import org.discu2.forum.repository.ReplyRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final TopicService topicService;
    private final PostService postService;

    public TextBlock.Reply createNewReply(@NonNull String topicId, @NonNull String accountId, @NonNull String content) throws DataNotFoundException {

        topicService.loadTopicById(topicId);
        postService.loadPostByTopicId(topicId);

        var now = LocalDateTime.now();
        var post = new TextBlock.Reply(
                null,
                topicId,
                accountId,
                now,
                now,
                content,
                Lists.newArrayList(),
                Lists.newArrayList());

        return replyRepository.save(post);
    }

    public List<TextBlock.Reply> loadRepliesByTopicId(@NonNull String topicId) throws DataNotFoundException {

        var replies = replyRepository.findByTopicId(topicId);

        if (replies.isEmpty()) throw new DataNotFoundException(TextBlock.Reply.class, "topicId", topicId);

        return replies;
    }

    public TextBlock.Reply loadById(@NonNull String id) throws DataNotFoundException {
        return replyRepository.findById(id).orElseThrow(() -> new DataNotFoundException(TextBlock.Reply.class, "id", id));
    }
}
