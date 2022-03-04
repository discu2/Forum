package org.discu2.forum.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.discu2.forum.exception.DataNotFoundException;
import org.discu2.forum.model.Board;
import org.discu2.forum.packet.CreateBoardRequestPacket;
import org.discu2.forum.service.BoardService;
import org.discu2.forum.util.JsonConverter;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/board")
@AllArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PreAuthorize("permitAll()")
    @GetMapping
    public void getAllBoards(HttpServletResponse response) throws IOException {

        var roles = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        var boards = boardService.loadAllBoards();

        if (!roles.contains("ADMIN"))
            boards.removeIf(b -> b.getPermissions().get("access").stream().anyMatch(r -> !roles.contains(r)));

        JsonConverter.PacketToJsonResponse(response, boards);

    }

    @PostMapping
    public void createBoard(HttpServletRequest request, HttpServletResponse response) throws IOException {

        var packet = JsonConverter.requestToPacket(request.getInputStream(), CreateBoardRequestPacket.class);
        var board = boardService.createNewBoard(packet.getGroupName(), packet.getName(), packet.getBasicPermissionRoles());

        for (var m : packet.getModeratorRoles())
            boardService.addPermissions(board.getGroupName(), board.getName(), Board.PERMISSION_MODERATOR, m);

    }

    @DeleteMapping("/{groupName}/{name}")
    public void deleteBoard(@PathVariable String groupName, @PathVariable String name,
            HttpServletRequest request, HttpServletResponse response) throws DataNotFoundException {

        boardService.deleteByGroupNameAndName(groupName, name);

    }
}
