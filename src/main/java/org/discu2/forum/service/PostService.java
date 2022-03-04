package org.discu2.forum.service;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.discu2.forum.exception.DataNotFoundException;
import org.discu2.forum.model.Account;
import org.discu2.forum.model.TextBlock;
import org.discu2.forum.repository.PostRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final TopicService topicService;
    private final AccountService accountService;
    private final MongoTemplate mongoTemplate;

    public TextBlock.Post createNewPost(@NonNull String topicId,
                                        @NonNull String username,
                                        @NonNull String content,
                                        Boolean isOriginPost) throws UsernameNotFoundException, DataNotFoundException {

        var accountId = (Account) accountService.loadUserByUsername(username);

        return createNewPost(topicId, accountId.getId(), username, content, isOriginPost);
    }

    public TextBlock.Post createNewPost(@NonNull String topicId,
                                        @NonNull String accountId,
                                        @NonNull String username,
                                        @NonNull String content,
                                        Boolean isOriginPost) throws UsernameNotFoundException, DataNotFoundException {

        var now = new Date().getTime();
        var topic = topicService.loadTopicById(topicId);
        var post = new TextBlock.Post(
                null,
                topicId,
                accountId,
                username,
                now,
                now,
                content,
                isOriginPost,
                Lists.newArrayList(),
                Lists.newArrayList());

        topicService.updateLastPoster(topic, accountId, username, now);

        return postRepository.save(post);
    }

    public TextBlock.Post editPostContentById(@NonNull String postId, String content) throws DataNotFoundException {

        var post = loadById(postId);

        post.setContent(content);
        post.setLastEditTime(new Date().getTime());

        return postRepository.save(post);
    }

    public List<TextBlock.Post> loadPostsByTopicId(@NonNull String topicId, int page, int pageSize) {

        var op = postRepository.findByTopicIdAndOriginPostIsTrue(topicId);
        List<TextBlock.Post> posts = op.isPresent() ? Lists.newArrayList(op.get()) : Lists.newArrayList();
        var query = Query.query(Criteria.where("topicId").is(topicId).and("originPost").is(false))
                .with(Sort.by("lastPostTime").ascending())
                .skip((page - 1) * pageSize).
                limit(pageSize);

        posts.addAll(mongoTemplate.find(query, TextBlock.Post.class));

        return posts;
    }

    public TextBlock.Post loadOriginPostsByTopicId(@NonNull String topicId) throws DataNotFoundException {
        return postRepository.findByTopicIdAndOriginPostIsTrue(topicId).orElseThrow(() -> new DataNotFoundException(TextBlock.Post.class, "topicId", topicId));
    }

    public TextBlock.Post loadById(@NonNull String id) throws DataNotFoundException {
        return postRepository.findById(id).orElseThrow(() -> new DataNotFoundException(TextBlock.Post.class, "id", id));
    }
}
