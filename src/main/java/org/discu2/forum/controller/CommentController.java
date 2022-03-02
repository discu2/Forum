package org.discu2.forum.controller;

import lombok.AllArgsConstructor;
import org.discu2.forum.packet.TextBlockRequestPacket;
import org.discu2.forum.service.CommentService;
import org.discu2.forum.util.JsonConverter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
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

    @PreAuthorize("hasPermission(#masterId, 'Comment', 'comment')")
    @PostMapping("/{masterId}")
    public void createComment(@PathVariable("masterId") String masterId,
                           HttpServletRequest request, HttpServletResponse response) throws IOException {

        var packet = JsonConverter.requestToPacket(request.getInputStream(), TextBlockRequestPacket.class);
        var username = request.getUserPrincipal().getName();

        commentService.createNewComment(masterId, username, packet.getContent());



    }

    public void getCommentByMasterId(@PathVariable("masterId") String masterId, HttpServletResponse response) throws IOException {

        var comments = commentService.loadCommentsByMasterId(masterId);

        JsonConverter.PacketToJsonResponse(response, comments);
    }
}
