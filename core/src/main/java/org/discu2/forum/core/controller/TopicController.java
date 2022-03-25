package org.discu2.forum.core.controller;

import lombok.AllArgsConstructor;
import org.discu2.forum.api.exception.BadPacketFormatException;
import org.discu2.forum.api.packet.TextBlockRequestPacket;
import org.discu2.forum.core.service.TopicService;
import org.discu2.forum.api.util.JsonConverter;
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
    @PostMapping(value = "/{boardId}", produces = "application/json")
    public void createPost(@PathVariable("boardId") String boardId,
                           HttpServletRequest request, HttpServletResponse response) throws IOException {

        var packet = JsonConverter.requestToPacket(request.getInputStream(), TextBlockRequestPacket.Post.class);
        var username = request.getUserPrincipal().getName();

        try {
            topicService.createNewTopicWithPost(boardId, username, packet.getTitle(), packet.getContent());
        } catch (NullPointerException e) {
            throw new BadPacketFormatException(e.getMessage());
        }

    }

    @PreAuthorize("hasPermission(#boardId, 'Board', 'access')")
    @GetMapping("/{boardId}")
    public void getTopics(@RequestParam int page, @RequestParam(value = "page_size", required = false) Integer pageSize,
                          @PathVariable("boardId") String boardId,
                          HttpServletRequest request, HttpServletResponse response) throws IOException {

        if (pageSize == null) pageSize = 30;

        var topics = topicService.loadTopicsByBoard(boardId, page, pageSize);

        JsonConverter.PacketToJsonResponse(response, topics);

    }

}


