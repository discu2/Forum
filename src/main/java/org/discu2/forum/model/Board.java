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

    public static final List<String> PERMISSIONS = Lists.newArrayList("moderator", "access", "post", "reply", "comment", "edit");
    private static RoleRepository roleRepository = SpringContext.getBean(RoleRepository.class);

    @Id
    private String id;

    private String name;
    private String groupName;

    /**
     * <PERMISSIONS, Set<RoleIds>>
     */
    private Map<String, Set<String>> permissions;

}