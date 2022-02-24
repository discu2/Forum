package org.discu2.forum.controller;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.discu2.forum.model.TextBlock;
import org.discu2.forum.model.Topic;
import org.discu2.forum.repository.CommentRepository;
import org.discu2.forum.repository.PostRepository;
import org.discu2.forum.repository.ReplyRepository;
import org.discu2.forum.repository.TopicRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@AllArgsConstructor
public class TopicController {

    private final TopicRepository topicRepository;
    private final PostRepository postRepository;
    private final ReplyRepository replyRepository;
    private final CommentRepository commentRepository;

    @PostMapping("/post_topic")
    public String postTopic(@RequestBody Map<String, String> map) {

        var topic = new Topic(
                null,
                map.get("boardId"),
                null, //don't have a post id now.
                map.get("title"),
                false,
                1,
                LocalDateTime.now()
        );

        topicRepository.save(topic);

        var textBlock = new TextBlock.Post(
                null,
                topic.getId(),
                topic.getPosterId(),
                topic.getCreateDateTime(),
                topic.getCreateDateTime(),
                map.get("topicContent"),
                Lists.newArrayList(),
                Lists.newArrayList(),
                Lists.newArrayList()
        );

        postRepository.save(textBlock);

        return "";
    }

    @PostMapping("/reply_topic")
    public String replyTopic(@RequestBody Map<String, String> map) {

        var topic = new Topic(
                null,
                map.get("boardId"),
                null, //don't have a post id now.
                map.get("title"),
                false,
                1,
                LocalDateTime.now()
        );

        topicRepository.save(topic);

        var textBlock = new TextBlock.Reply(
                null,
                topic.getId(),
                topic.getPosterId(),
                topic.getCreateDateTime(),
                topic.getCreateDateTime(),
                map.get("topicContent"),
                Lists.newArrayList(),
                Lists.newArrayList(),
                Lists.newArrayList()
        );

        replyRepository.save(textBlock);

        return "";
    }

    @PostMapping("/comment_post")
    public String commentPost(@RequestBody Map<String, String> map) {

        var post = postRepository.findById(map.get("id"));
        if (post.isPresent()) {

            var textBlock = new TextBlock.Comment(
                    null,
                    post.get().getTopicId(),
                    post.get().getOwnerId(),
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    map.get("commentContent"),
                    Lists.newArrayList(),
                    Lists.newArrayList()
            );

            var commentList = post.get().getComments();
            commentList.add(textBlock);
            post.get().setComments(commentList);

            postRepository.save(post.get());

        } else {

            return "";
        }

        return "";
    }

    @PostMapping("/comment_reply")
    public String commentReply(@RequestBody Map<String, String> map) {

        var reply = replyRepository.findById(map.get("id"));
        if (reply.isPresent()) {

            var textBlock = new TextBlock.Comment(
                    null,
                    reply.get().getTopicId(),
                    reply.get().getOwnerId(),
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    map.get("commentContent"),
                    Lists.newArrayList(),
                    Lists.newArrayList()
            );

            var commentList = reply.get().getComments();
            commentList.add(textBlock);
            reply.get().setComments(commentList);

            replyRepository.save(reply.get());

        } else {

            return "";
        }

        return "";
    }


}


