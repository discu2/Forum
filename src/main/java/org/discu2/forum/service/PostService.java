package org.discu2.forum.service;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.discu2.forum.exception.DataNotFoundException;
import org.discu2.forum.model.TextBlock;
import org.discu2.forum.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final TopicService topicService;

    public TextBlock.Post createNewPost(@NonNull String boardGroupName, @NonNull String boardName, @NonNull String accountId, @NonNull String title, @NonNull String content) throws DataNotFoundException {

        var topic = topicService.addNewTopic(boardGroupName, boardName, accountId, title);
        var post = new TextBlock.Post(
                null,
                topic.getId(),
                accountId,
                topic.getCreateDateTime(),
                topic.getCreateDateTime(),
                content,
                Lists.newArrayList(),
                Lists.newArrayList());

        return postRepository.save(post);
    }

    public TextBlock.Post loadPostByTopicId(@NonNull String topicId) throws DataNotFoundException {
        return postRepository.findByTopicId(topicId).orElseThrow(() -> new DataNotFoundException(TextBlock.Post.class, "topicId", topicId));
    }

    public TextBlock.Post loadById(@NonNull String id) throws DataNotFoundException {
        return postRepository.findById(id).orElseThrow(() -> new DataNotFoundException(TextBlock.Post.class, "id", id));
    }
}
