package org.discu2.forum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

@Data
@AllArgsConstructor
@Document
public class Role {

    private static final String[] PERMISSIONS = new String[] { "access", "post", "reply", "comment", "edit_post", "edit_reply", "edit_comment", "moderate" };

    @Id
    private String id;

    private String name;

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {

        var authorities = new HashSet<SimpleGrantedAuthority>();
        for (var p : PERMISSIONS) authorities.add(new SimpleGrantedAuthority(p + "_" + name));

        if (name.equals("DEFAULT")) authorities.add(new SimpleGrantedAuthority("account_self"));

        return authorities;
    }
}
