package org.discu2.forum.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


@Data
@AllArgsConstructor
@Document
public class Role {

    @Id
    private String id;

    private String name;

    public SimpleGrantedAuthority getGrantedAuthorities() {
        return new SimpleGrantedAuthority("ROLE_" + id);
    }
}
