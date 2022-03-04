package org.discu2.forum.controller;

import lombok.AllArgsConstructor;
import org.discu2.forum.packet.TextBlockRequestPacket;
import org.discu2.forum.service.CommentService;
import org.discu2.forum.util.JsonConverter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/comment")
@AllArgsConstructor
public class CommentController {

    private CommentService commentService;

    @PreAuthorize("hasPermission(#postId, 'Post', 'comment')")
    @PostMapping("/{postId}")
    public void createComment(@PathVariable("postId") String postId,
                           HttpServletRequest request, HttpServletResponse response) throws IOException {

        var packet = JsonConverter.requestToPacket(request.getInputStream(), TextBlockRequestPacket.class);
        var username = request.getUserPrincipal().getName();

        commentService.createNewComment(postId, username, packet.getContent());

    }

    @PreAuthorize("hasPermission(#postId, 'Post', 'access')")
    @GetMapping("/{postId}")
    public void getComments(@PathVariable("postId") String postId, HttpServletResponse response) throws IOException {

        var comments = commentService.loadCommentsByPostId(postId);

        JsonConverter.PacketToJsonResponse(response, comments);
    }
}
