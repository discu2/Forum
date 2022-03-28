package org.discu2.forum.common.model;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@Document
public class Board {

    public static final String PERMISSION_ACCESS = "access";
    public static final String PERMISSION_POST = "post";
    public static final String PERMISSION_REPLY = "reply";
    public static final String PERMISSION_COMMENT = "comment";
    public static final String PERMISSION_EDIT = "edit";
    public static final String PERMISSION_MODERATOR = "moderator";

    public static final List<String> PERMISSIONS = Lists.newArrayList(PERMISSION_ACCESS, PERMISSION_POST, PERMISSION_REPLY, PERMISSION_COMMENT, PERMISSION_EDIT, PERMISSION_MODERATOR);
    public static final List<String> BASIC_PERMISSIONS = Lists.newArrayList(PERMISSION_ACCESS, PERMISSION_POST, PERMISSION_REPLY, PERMISSION_COMMENT, PERMISSION_EDIT);

    @Id
    private String id;

    private String name;
    private String groupName;

    /**
     * <PERMISSIONS, Set<RoleIds>>
     */
    private Map<String, Set<String>> permissions;

}