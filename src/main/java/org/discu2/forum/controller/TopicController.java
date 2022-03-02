package org.discu2.forum.controller;

import lombok.AllArgsConstructor;
import org.discu2.forum.service.TopicService;
import org.discu2.forum.util.JsonConverter;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/topic")
@AllArgsConstructor
public class TopicController {

    private final TopicService topicService;

    @PostAuthorize("hasPermission(#boardId, 'Board', 'access') or hasAuthority('ADMIN')")
    @GetMapping("/get/{boardId}")
    public void getTopics(@RequestParam int page, @RequestParam int page_size,
                          @PathVariable("boardId") String boardId,
                          HttpServletRequest request, HttpServletResponse response) throws IOException {

        var topics = topicService.loadTopicsByBoard(boardId, page, page_size);

        JsonConverter.PacketToJsonResponse(response, topics);

    }

}


