package org.discu2.forum.controller;

import lombok.AllArgsConstructor;
import org.discu2.forum.model.TextBlock;
import org.discu2.forum.packet.TextBlockRequestPacket;
import org.discu2.forum.service.PostService;
import org.discu2.forum.util.JsonConverter;
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

    @PreAuthorize("hasPermission(#boardId, 'Board', 'post')")
    @PostMapping("/{boardId}")
    public void createPost(@PathVariable("boardId") String boardId,
            HttpServletRequest request, HttpServletResponse response) throws IOException {

        var packet = JsonConverter.requestToPacket(request.getInputStream(), TextBlockRequestPacket.Post.class);
        var username = request.getUserPrincipal().getName();

        postService.createNewPost(boardId, username, packet.getTitle(), packet.getContent(), true);

    }

    @PreAuthorize("hasPermission(#boardId, 'Board', 'reply')")
    @PostMapping("/reply/{boardId}")
    public void createReply(@PathVariable("boardId") String boardId,
                           HttpServletRequest request, HttpServletResponse response) throws IOException {

        var packet = JsonConverter.requestToPacket(request.getInputStream(), TextBlockRequestPacket.Post.class);
        var username = request.getUserPrincipal().getName();

        postService.createNewPost(boardId, username, packet.getTitle(), packet.getContent(), false);

    }

    @GetMapping("/get/{topicId}")
    public void getPostByTopicId(@PathVariable("topicId") String topicId, HttpServletResponse response) throws IOException {

        var topic = postService.loadPostByTopicId(topicId);

        JsonConverter.PacketToJsonResponse(response, topic);
    }

    @PutMapping("/edit/{topicId}")
    public void editPost(@PathVariable("topicId") String topicId ,HttpServletRequest request, HttpServletResponse response) throws IOException {

        var packet = JsonConverter.requestToPacket(request.getInputStream(), TextBlock.Post.class);

        postService.editPostContentById(topicId, packet.getContent());

    }

}
