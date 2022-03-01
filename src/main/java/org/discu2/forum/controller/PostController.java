package org.discu2.forum.controller;

import lombok.AllArgsConstructor;
import org.discu2.forum.exception.DataNotFoundException;
import org.discu2.forum.model.TextBlock;
import org.discu2.forum.packet.TextBlockRequestPacket;
import org.discu2.forum.service.PostService;
import org.discu2.forum.util.JsonConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/post")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/create")
    public void createPost(HttpServletRequest request) throws IOException {

        var packet = JsonConverter.requestToPacket(request.getInputStream(), TextBlockRequestPacket.Post.class);
        var username = request.getUserPrincipal().getName();

        postService.createNewPost(packet.getBoardGroupName(), packet.getBoardName(), username, packet.getTitle(), packet.getContent());

    }

    @GetMapping("/get")
    public void getPostByTopicId(@RequestParam String topicId, HttpServletResponse response) throws IOException {

        var topic = postService.loadPostByTopicId(topicId);

        JsonConverter.PacketToJsonResponse(response.getOutputStream(), topic);
    }

    @PutMapping("edit")
    public void editPost(@RequestParam String topicId ,HttpServletRequest request) throws IOException {

        var packet = JsonConverter.requestToPacket(request.getInputStream(), TextBlock.Post.class);

        postService.editPostContentById(topicId, packet.getContent());

    }

}
