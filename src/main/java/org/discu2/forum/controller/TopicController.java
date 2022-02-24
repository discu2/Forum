package org.discu2.forum.controller;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.discu2.forum.model.TextBlock;
import org.discu2.forum.model.Topic;
import org.discu2.forum.repository.PostRepository;
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

        topicRepository.insert(topic);

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


}


