package org.discu2.forum.service;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.discu2.forum.exception.AlreadyExistException;
import org.discu2.forum.exception.DataNotFoundException;
import org.discu2.forum.model.Board;
import org.discu2.forum.repository.BoardRepository;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.discu2.forum.model.Board.PERMISSIONS;

@Service
@AllArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final RoleService roleService;

    public void addPermissions(String boardGroupName, String boardName, String permission, String roleName) throws DataNotFoundException {

        if (!PERMISSIONS.contains(permission)) throw new DataNotFoundException("PERMISSION", "name", permission);
        var role = roleService.loadRoleByName(roleName);
        var board = loadBoardByGroupNameAndName(boardGroupName, boardName);

        if (!board.getPermissions().containsKey(permission)) board.getPermissions().put(permission, new HashSet<>());

        board.getPermissions().get(permission).add(role.getName());
    }

    /**
     *
     * @param name - board name
     * @param groupName
     * @param roleNames - leave this empty will auto init the role with DEFAULT
     * @return - a new board with the permissions
     */
    public Board createNewBoard(@NonNull String name, @NonNull String groupName, String... roleNames) throws DataNotFoundException, AlreadyExistException {

        if (boardRepository.findByGroupNameAndAndName(groupName, name).isPresent()) throw new AlreadyExistException(Board.class);
        var board = new Board(null, name, groupName, new HashMap<>());

        if (roleNames.length == 0) {

            for (int i = 1, length = PERMISSIONS.toArray().length; i<length; i++)
                addPermissions(groupName, name, PERMISSIONS.get(i), "DEFAULT");

        } else {

            for (int i = 1, length = PERMISSIONS.toArray().length; i < length; i++)
                for (var roleName : roleNames) addPermissions(groupName, name, PERMISSIONS.get(i), roleName);

        }

        return boardRepository.save(board);
    }

    public Set<Board> loadBoardsByGroupName(@NonNull String groupName) throws DataNotFoundException {

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

}
