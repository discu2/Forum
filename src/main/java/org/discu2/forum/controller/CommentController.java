package org.discu2.forum.controller;

import lombok.AllArgsConstructor;
import org.discu2.forum.packet.TextBlockRequestPacket;
import org.discu2.forum.service.CommentService;
import org.discu2.forum.util.JsonConverter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/comment")
@AllArgsConstructor
public class CommentController {

    private CommentService commentService;

    @PreAuthorize("hasPermission(#postId, 'Post', 'comment')")
    @PostMapping(value = "/{postId}", produces = "application/json")
    public void createComment(@PathVariable("postId") String postId,
                           HttpServletRequest request, HttpServletResponse response) throws IOException {

        var packet = JsonConverter.requestToPacket(request.getInputStream(), TextBlockRequestPacket.class);
        var username = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        commentService.createNewComment(postId, username, packet.getContent());

    }

    @PreAuthorize("hasPermission(#postId, 'Post', 'access')")
    @GetMapping("/{postId}")
    public void getComments(@PathVariable("postId") String postId,
                            @RequestParam() int page, @RequestParam(value = "page_size", required = false) Integer pageSize,
                            HttpServletResponse response) throws IOException {

        if (pageSize == null) pageSize = 20;

        var comments = commentService.loadCommentsByPostId(postId, page, pageSize);

        JsonConverter.PacketToJsonResponse(response, comments);
    }
}
