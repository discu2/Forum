package org.discu2.forum.core.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.discu2.forum.common.model.Board;
import org.discu2.forum.common.exception.AlreadyExistException;
import org.discu2.forum.common.exception.DataNotFoundException;
import org.discu2.forum.core.repository.BoardRepository;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.discu2.forum.common.model.Board.*;

@Service
@AllArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public Board addPermissions(@NonNull String boardGroupName, @NonNull String boardName, @NonNull String permission, @NonNull String roleId)
            throws DataNotFoundException {

        if (!PERMISSIONS.contains(permission)) throw new DataNotFoundException("PERMISSION", "name", permission);

        //need check role is present
        var board = loadBoardByGroupNameAndName(boardGroupName, boardName);

        if (!board.getPermissions().containsKey(permission)) board.getPermissions().put(permission, new HashSet<>());

        board.getPermissions().get(permission).add(roleId);

        return boardRepository.save(board);
    }

    /**
     *
     * @param name - board name
     * @param groupName
     * @param roleIds - leave this empty will auto init with DEFAULT role
     * @return - a new board with the permissions
     */
    public Board createNewBoard(@NonNull String groupName, @NonNull String name, String... roleIds) throws DataNotFoundException, AlreadyExistException {

        if (boardRepository.findByGroupNameAndAndName(groupName, name).isPresent()) throw new AlreadyExistException(Board.class);
        var board = boardRepository.save(new Board(null, name, groupName, new HashMap<>()));

        if (roleIds == null || roleIds.length == 0) {

            addPermissions(groupName, name, PERMISSION_ACCESS, "ANONYMOUS");
            for (var p : BASIC_PERMISSIONS)
                addPermissions(groupName, name, p, "DEFAULT");

        } else {

            for (var p : BASIC_PERMISSIONS)
                for (var roleName : roleIds) {
                    if (roleName == "ANONYMOUS" && !p.equals(PERMISSION_ACCESS)) continue;
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
