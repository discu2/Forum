package org.discu2.forum.controller;

import lombok.AllArgsConstructor;
import org.discu2.forum.packet.TextBlockRequestPacket;
import org.discu2.forum.service.TopicService;
import org.discu2.forum.util.JsonConverter;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/topic")
@AllArgsConstructor
public class TopicController {

    private final TopicService topicService;

    @PreAuthorize("hasPermission(#boardId, 'Board', 'post')")
    @PostMapping("/{boardId}")
    public void createPost(@PathVariable("boardId") String boardId,
                           HttpServletRequest request, HttpServletResponse response) throws IOException {

        var packet = JsonConverter.requestToPacket(request.getInputStream(), TextBlockRequestPacket.Post.class);
        var username = request.getUserPrincipal().getName();

        topicService.createNewTopicWithPost(boardId, username, packet.getTitle(), packet.getContent());

    }

    @PostAuthorize("hasPermission(#boardId, 'Board', 'access') or hasAuthority('ADMIN')")
    @GetMapping("/{boardId}")
    public void getTopics(@RequestParam int page, @RequestParam int page_size,
                          @PathVariable("boardId") String boardId,
                          HttpServletRequest request, HttpServletResponse response) throws IOException {

        var topics = topicService.loadTopicsByBoard(boardId, page, page_size);

        JsonConverter.PacketToJsonResponse(response, topics);

    }

}


