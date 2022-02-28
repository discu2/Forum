package org.discu2.forum.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.discu2.forum.exception.DataNotFoundException;
import org.discu2.forum.model.Account;
import org.discu2.forum.model.Topic;
import org.discu2.forum.repository.TopicRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;
    private final AccountService accountService;
    private final BoardService boardService;

    public Topic addNewTopic(@NonNull String boardGroupName, @NonNull String boardName, @NonNull String accountName, @NonNull String title) throws DataNotFoundException {
        return addNewTopic(boardService.loadBoardByGroupNameAndName(boardGroupName, boardName).getId(),
                ((Account)accountService.loadUserByUsername(accountName)).getId(), title);
    }

    public Topic addNewTopic(@NonNull String boardId, @NonNull String posterId, @NonNull String title) {

        var topic = new Topic(null, boardId, posterId, title, false, 0, LocalDateTime.now());

        return topicRepository.save(topic);
    }

    public Topic loadTopicById(@NonNull String id) throws DataNotFoundException {
        return topicRepository.findById(id).orElseThrow(() -> new DataNotFoundException(Topic.class, "id", id));
    }

}
