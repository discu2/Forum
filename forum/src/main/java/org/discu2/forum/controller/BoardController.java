package org.discu2.forum.controller;

import lombok.AllArgsConstructor;
import org.discu2.forum.api.model.Board;
import org.discu2.forum.api.exception.BadPacketFormatException;
import org.discu2.forum.api.exception.DataNotFoundException;
import org.discu2.forum.api.packet.CreateBoardRequestPacket;
import org.discu2.forum.service.BoardService;
import org.discu2.forum.api.util.JsonConverter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/board")
@AllArgsConstructor
public class BoardController {

    private final BoardService boardService;
    //private final RoleService roleService;

    @PreAuthorize("permitAll()")
    @GetMapping
    public void getAllBoards(HttpServletResponse response) throws IOException {

        var roles = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        var boards = boardService.loadAllBoards();

        if (!roles.contains(new SimpleGrantedAuthority("ADMIN")))

            boards.removeIf(b -> !b.getPermissions().get(Board.PERMISSION_ACCESS).stream().anyMatch(roleId -> {
                try {
                    //var roleName = roleService.loadRoleById(roleId).getName();
                    return roles.contains(new SimpleGrantedAuthority("ROLE_" + roleId));
                } catch (Exception e) {
                    return false;
                }
            }));

        JsonConverter.PacketToJsonResponse(response, boards);

    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(produces = "application/json")
    public void createBoard(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Board board;
        var packet = JsonConverter.requestToPacket(request.getInputStream(), CreateBoardRequestPacket.class);

        try {
            board = boardService.createNewBoard(packet.getGroupName(), packet.getName(), packet.getBasicPermissionRoles());
        } catch (NullPointerException e) {
            throw new BadPacketFormatException(e.getMessage());
        }

        for (var m : packet.getModeratorRoles())
            boardService.addPermissions(board.getGroupName(), board.getName(), Board.PERMISSION_MODERATOR, m);

    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{groupName}/{name}")
    public void deleteBoard(@PathVariable String groupName, @PathVariable String name,
            HttpServletRequest request, HttpServletResponse response) throws DataNotFoundException {

        boardService.deleteByGroupNameAndName(groupName, name);

    }
}
