package org.discu2.forum.core.service;

import lombok.NonNull;
import org.discu2.forum.common.model.TextBlock;
import org.discu2.forum.common.model.Topic;
import org.discu2.forum.common.exception.DataNotFoundException;
import org.discu2.forum.core.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
    private final BoardService boardService;

    public TopicService(TopicRepository topicRepository, MongoTemplate mongoTemplate, BoardService boardService) {
        this.topicRepository = topicRepository;
        this.mongoTemplate = mongoTemplate;
        this.boardService = boardService;
    }

    public TextBlock.Post createNewTopicWithPost(@NonNull String boardId,
                                                 @NonNull String username,
                                                 @NonNull String title,
                                                 @NonNull String content) throws UsernameNotFoundException, DataNotFoundException {

        var topic = createNewTopic(boardId, username, title);

        return postService.createNewPost(topicRepository.save(topic).getId(), username, content, true);
    }

    private Topic createNewTopic(@NonNull String boardId,
                                 @NonNull String username,
                                 @NonNull String title) throws UsernameNotFoundException {

        var now = new Date().getTime();
        var topic = new Topic(null,
                boardId,
                username,
                title,
                "",
                now,
                0L,
                0
                );

        return topicRepository.save(topic);
    }

    public List<Topic> loadTopicsByBoard(@NonNull String boardGroupName, @NonNull String boardName, int page, int pageSize) throws DataNotFoundException {

        var boardId = boardService.loadBoardByGroupNameAndName(boardGroupName, boardName).getId();

        return loadTopicsByBoard(boardId, page, pageSize);
    }

    public List<Topic> loadPinnedTopicByBoardId(@NonNull String boardId, int page, int pageSize) throws DataNotFoundException {

        boardService.loadBoardById(boardId);
        var query = Query.query(Criteria.where("boardId").is(boardId).and("pinnedOrder").gt(0))
                //.with(Sort.by("pinnedOrder").ascending())
                .withHint("default_desc")
                .skip((page - 1) * pageSize)
                .limit(pageSize);

        return mongoTemplate.find(query, Topic.class);
    }

    /**
     *
     * @return A List of topic with pinned topic(s) on top
     */
    public List<Topic> loadTopicsByBoard(@NonNull String boardId, int page, int pageSize) throws DataNotFoundException {

        var topics = loadPinnedTopicByBoardId(boardId, page, pageSize);
        var query = Query.query(Criteria.where("boardId").is(boardId).and("pinnedOrder").lte(0))
                //.with(Sort.by("lastPostTime").descending())
                .withHint("default_desc")
                .skip((page - 1) * pageSize).
                limit(pageSize- topics.size());

        topics.addAll(mongoTemplate.find(query, Topic.class));

        return topics;
    }

    public Topic loadTopicById(@NonNull String id) throws DataNotFoundException {
        return topicRepository.findById(id).orElseThrow(() -> new DataNotFoundException(Topic.class, "id", id));
    }

    public Topic updateLastPoster(Topic topic, @NonNull String username, long time) {

        topic.setLastPosterUsername(username);
        topic.setLastPostTime(time);

        return topicRepository.save(topic);
    }
}
