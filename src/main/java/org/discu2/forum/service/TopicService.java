package org.discu2.forum.service;

import lombok.NonNull;
import org.discu2.forum.exception.DataNotFoundException;
import org.discu2.forum.model.Account;
import org.discu2.forum.model.TextBlock;
import org.discu2.forum.model.Topic;
import org.discu2.forum.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TopicService {

    private final TopicRepository topicRepository;

    @Autowired
    @Lazy
    private PostService postService;
    private final MongoTemplate mongoTemplate;
    private final AccountService accountService;
    private final BoardService boardService;

    public TopicService(TopicRepository topicRepository, MongoTemplate mongoTemplate, AccountService accountService, BoardService boardService) {
        this.topicRepository = topicRepository;
        this.mongoTemplate = mongoTemplate;
        this.accountService = accountService;
        this.boardService = boardService;
    }

    public TextBlock.Post createNewTopicWithPost(@NonNull String boardId,
                                                 @NonNull String username,
                                                 @NonNull String title,
                                                 @NonNull String content) throws UsernameNotFoundException, DataNotFoundException {

        var accountId = ((Account) accountService.loadUserByUsername(username)).getId();
        var topic = createNewTopic(boardId, accountId, username, title);

        return postService.createNewPost(topicRepository.save(topic).getId(), accountId, username, content, true);
    }

    private Topic createNewTopic(@NonNull String boardId,
                                 @NonNull String accountId,
                                 @NonNull String username,
                                 @NonNull String title) throws UsernameNotFoundException {

        var now = new Date().getTime();
        var topic = new Topic(null,
                boardId,
                accountId,
                username,
                title,
                "",
                "",
                now,
                0L,
                false,
                0
                );

        return topicRepository.save(topic);
    }

    public List<Topic> loadTopicsByBoard(@NonNull String boardGroupName, @NonNull String boardName, int page, int pageSize) throws DataNotFoundException {

        var boardId = boardService.loadBoardByGroupNameAndName(boardGroupName, boardName).getId();

        return loadTopicsByBoard(boardId, page, pageSize);
    }

    public List<Topic> loadPinnedTopicByBoardId(@NonNull String boardId, int page, int pageSize) {

        var query = Query.query(Criteria.where("boardId").is(boardId).and("pinned").is(true))
                .with(Sort.by("pinnedOrder").ascending())
                .skip((page - 1) * pageSize).
                limit(pageSize);

        return mongoTemplate.find(query, Topic.class);
    }

    /**
     *
     * @return A List of topic with pinned topic(s) on top
     */
    public List<Topic> loadTopicsByBoard(@NonNull String boardId, int page, int pageSize) {

        var topics = loadPinnedTopicByBoardId(boardId, page, pageSize);
        var query = Query.query(Criteria.where("boardId").is(boardId).and("pinned").is(false))
                .with(Sort.by("lastPostTime").descending())
                .skip((page - 1) * pageSize).
                limit(pageSize- topics.size());

        topics.addAll(mongoTemplate.find(query, Topic.class));

        return topics;
    }

    public Topic loadTopicById(@NonNull String id) throws DataNotFoundException {
        return topicRepository.findById(id).orElseThrow(() -> new DataNotFoundException(Topic.class, "id", id));
    }

    public Topic updateLastPoster(Topic topic, @NonNull String accountId, @NonNull String username, long time) {

        topic.setLastPosterId(accountId);
        topic.setLastPosterUsername(username);
        topic.setLastPostTime(time);

        return topicRepository.save(topic);
    }
}
