package org.discu2.forum.model;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.discu2.forum.repository.RoleRepository;
import org.discu2.forum.util.SpringContext;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@Data
@AllArgsConstructor
@Document
public class Board {

    private static final List<String> PERMISSIONS = Lists.newArrayList("moderator", "access", "post", "reply", "comment", "edit");
    private static RoleRepository roleRepository = SpringContext.getBean(RoleRepository.class);

    @Id
    private String id;

    private String name;
    private String groupName;

    /**
     * <PERMISSIONS, Set<RoleIds>>
     */
    private Map<String, Set<String>> permissions;

    public void addPermissions(String permission, String roleName) throws RuntimeException {

        if (!PERMISSIONS.contains(permission)) throw new RuntimeException("permission:\"" + permission + "\" not found");
        var role = roleRepository.findByName(roleName).orElseThrow(() -> new RuntimeException("role:\"" + roleName + "\" not found"));

        if (!permissions.containsKey(permission)) permissions.put(permission, new HashSet<>());

        permissions.get(permission).add(role.getName());
    }

    /**
     *
     * @param name - board name
     * @param groupName
     * @param roleNames - leave this empty will auto init the role with DEFAULT
     * @return - a new board with the permissions
     */
    public static Board createNewBoard(String name, String groupName, String... roleNames) throws RuntimeException {

        var board = new Board(null, name, groupName, new HashMap<>());

        if (roleNames.length == 0) {

            for (int i = 1, length = PERMISSIONS.toArray().length; i<length; i++) {
                board.addPermissions(PERMISSIONS.get(i), "DEFAULT");
            }

            return board;
        }

        for (int i = 1, length = PERMISSIONS.toArray().length; i<length; i++)
            for (var roleName : roleNames) board.addPermissions(PERMISSIONS.get(i), roleName);

        return board;
    }

}