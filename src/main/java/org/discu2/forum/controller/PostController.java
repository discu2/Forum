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

    @PreAuthorize("hasPermission(#topicId, 'Topic', 'reply')")
    @PostMapping("/reply/{topicId}")
    public void createReply(@PathVariable("topicId") String topicId,
                            HttpServletRequest request, HttpServletResponse response) throws IOException {

        var packet = JsonConverter.requestToPacket(request.getInputStream(), TextBlockRequestPacket.Post.class);
        var username = request.getUserPrincipal().getName();

        postService.createNewPost(topicId, username, packet.getTitle(), packet.getContent(), false);

    }

    @GetMapping("/get/{topicId}")
    public void getPosts(@PathVariable("topicId") String topicId, @RequestParam int page, @RequestParam("page_size") int pageSize,
                                 HttpServletResponse response) throws IOException {

        var posts = postService.loadPostsByTopicId(topicId, page, pageSize);

        JsonConverter.PacketToJsonResponse(response, posts);
    }

    @PutMapping("/edit/{topicId}")
    public void editPost(@PathVariable("topicId") String topicId ,HttpServletRequest request, HttpServletResponse response) throws IOException {

        var packet = JsonConverter.requestToPacket(request.getInputStream(), TextBlock.Post.class);

        postService.editPostContentById(topicId, packet.getContent());

    }

}
