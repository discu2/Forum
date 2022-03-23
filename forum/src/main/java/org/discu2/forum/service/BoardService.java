package org.discu2.forum.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.discu2.forum.exception.AlreadyExistException;
import org.discu2.forum.exception.DataNotFoundException;
import org.discu2.forum.model.Board;
import org.discu2.forum.repository.BoardRepository;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.discu2.forum.model.Board.*;

@Service
@AllArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final RoleService roleService;

    public Board addPermissions(@NonNull String boardGroupName, @NonNull String boardName, @NonNull String permission, @NonNull String roleName)
            throws DataNotFoundException {

        if (!PERMISSIONS.contains(permission)) throw new DataNotFoundException("PERMISSION", "name", permission);
        var role = roleService.loadRoleByName(roleName);
        var board = loadBoardByGroupNameAndName(boardGroupName, boardName);

        if (!board.getPermissions().containsKey(permission)) board.getPermissions().put(permission, new HashSet<>());

        board.getPermissions().get(permission).add(role.getId());

        return boardRepository.save(board);
    }

    /**
     *
     * @param name - board name
     * @param groupName
     * @param roleNames - leave this empty will auto init the role with DEFAULT
     * @return - a new board with the permissions
     */
    public Board createNewBoard(@NonNull String groupName, @NonNull String name, String... roleNames) throws DataNotFoundException, AlreadyExistException {

        if (boardRepository.findByGroupNameAndAndName(groupName, name).isPresent()) throw new AlreadyExistException(Board.class);
        var board = boardRepository.save(new Board(null, name, groupName, new HashMap<>()));

        if (roleNames.length == 0) {

            addPermissions(groupName, name, PERMISSION_ACCESS, "ROLE_ANONYMOUS");
            for (var p : BASIC_PERMISSIONS)
                addPermissions(groupName, name, p, "DEFAULT");

        } else {

            for (var p : BASIC_PERMISSIONS)
                for (var roleName : roleNames) {
                    if (roleName == "ROLE_ANONYMOUS" && !p.equals(PERMISSION_ACCESS)) continue;
                    addPermissions(groupName, name, p, roleName);
                }

        }

        return board;
    }

    public List<Board> loadAllBoards() {
        return boardRepository.findAll();
    }

    public List<Board> loadBoardsByGroupName(@NonNull String groupName) throws DataNotFoundException {

        var result = boardRepository.findByGroupName(groupName);

        if (result.isEmpty()) throw new DataNotFoundException(Board.class, "groupName", groupName);

        return result;
    }

    public Board loadBoardById(@NonNull String id) throws DataNotFoundException {
        return boardRepository.findById(id).orElseThrow(() -> new DataNotFoundException(Board.class, "id", id));
    }

    public Board loadBoardByGroupNameAndName(@NonNull String groupName, @NonNull String name) throws DataNotFoundException {
        return boardRepository.findByGroupNameAndAndName(groupName, name).orElseThrow(() -> new DataNotFoundException(Board.class, "groupName:name", groupName + ":" + name));
    }

    public void deleteByGroupNameAndName(@NonNull String groupName, @NonNull String name) throws DataNotFoundException, IllegalArgumentException {

        var board = loadBoardByGroupNameAndName(groupName, name);
        boardRepository.delete(board);
    }

}
