package org.discu2.forum.controller;

import lombok.AllArgsConstructor;
import org.discu2.forum.api.model.TextBlock;
import org.discu2.forum.api.exception.BadPacketFormatException;
import org.discu2.forum.api.packet.TextBlockRequestPacket;
import org.discu2.forum.service.PostService;
import org.discu2.forum.api.util.JsonConverter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/post")
@AllArgsConstructor
public class PostController {

    private final PostService postService;


    @PreAuthorize("hasPermission(#topicId, 'Topic', 'reply')")
    @PostMapping(value = "/{topicId}", produces = "application/json")
    public void createReply(@PathVariable("topicId") String topicId,
                            HttpServletRequest request, HttpServletResponse response) throws IOException {

        var packet = JsonConverter.requestToPacket(request.getInputStream(), TextBlockRequestPacket.Post.class);
        var username = request.getUserPrincipal().getName();

        try {
            postService.createNewPost(topicId, username, packet.getContent(), false);
        } catch (NullPointerException e) {
            throw new BadPacketFormatException(e.getMessage());
        }

    }

    @PreAuthorize("hasPermission(#topicId, 'Topic', 'access')")
    @GetMapping("/{topicId}")
    public void getPosts(@PathVariable("topicId") String topicId, @RequestParam int page, @RequestParam(value = "page_size" ,required = false) Integer pageSize,
                                 HttpServletResponse response) throws IOException {

        if (pageSize == null) pageSize = 10;

        var posts = postService.loadPostsByTopicId(topicId, page, pageSize);

        JsonConverter.PacketToJsonResponse(response, posts);
    }

    @PreAuthorize("hasPermission(#topicId, 'Topic', 'edit')")
    @PutMapping(value = "/{topicId}", produces = "application/json")
    public void editPost(@PathVariable("topicId") String topicId ,HttpServletRequest request, HttpServletResponse response) throws IOException {

        var packet = JsonConverter.requestToPacket(request.getInputStream(), TextBlock.Post.class);

        postService.editPostContentById(topicId, packet.getContent());

    }

}
