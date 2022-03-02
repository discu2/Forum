package org.discu2.forum.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.discu2.forum.exception.DataNotFoundException;
import org.discu2.forum.model.Account;
import org.discu2.forum.model.Topic;
import org.discu2.forum.repository.TopicRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;
    private final MongoTemplate mongoTemplate;
    private final AccountService accountService;
    private final BoardService boardService;

    public Topic addNewTopic(@NonNull String boardGroupName,
                             @NonNull String boardName,
                             @NonNull String username,
                             @NonNull String title) throws DataNotFoundException {

        return addNewTopic(boardService.loadBoardByGroupNameAndName(boardGroupName, boardName).getId(),
                username, title);
    }

    public Topic addNewTopic(@NonNull String boardId, @NonNull String username, @NonNull String title) {

        var accountId = ((Account) accountService.loadUserByUsername(username)).getId();
        var now = new Date().getTime();

        var topic = new Topic(null,
                boardId,
                accountId,
                username,
                title,
                accountId,
                username,
                now,
                now,
                false,
                0
                );

        return topicRepository.save(topic);
    }

    public List<Topic> loadTopicsByBoard(@NonNull String boardGroupName, @NonNull String boardName, int page, int pageSize) throws DataNotFoundException {

        var boardId = boardService.loadBoardByGroupNameAndName(boardGroupName, boardName).getId();

        var topics = loadPinnedTopicByBoardId(boardId, page, pageSize);
        var notPinnedTopics = loadTopicsByBoard(boardGroupName, page, pageSize - topics.size());

        topics.addAll(notPinnedTopics);

        return topics;
    }

    public List<Topic> loadPinnedTopicByBoardId(@NonNull String boardId, int page, int pageSize) {

        var query = Query.query(Criteria.where("boardId").is(boardId).and("pinned").is(true))
                .with(Sort.by("pinnedOrder").ascending())
                .skip((page - 1) * pageSize).
                limit(pageSize);

        return mongoTemplate.find(query, Topic.class);
    }

    public List<Topic> loadTopicsByBoard(@NonNull String boardId, int page, int pageSize) {

        var query = Query.query(Criteria.where("boardId").is(boardId).and("pinned").is(false))
                .with(Sort.by("lastPostTime").descending())
                .skip((page - 1) * pageSize).
                limit(pageSize);

        return mongoTemplate.find(query, Topic.class);
    }

    public Topic loadTopicById(@NonNull String id) throws DataNotFoundException {
        return topicRepository.findById(id).orElseThrow(() -> new DataNotFoundException(Topic.class, "id", id));
    }

}
