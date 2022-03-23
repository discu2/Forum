package org.discu2.forum.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@Document
public abstract class Account implements UserDetails {

    public static final int MAX_REFRESH_TOKENS = 6;

    @Id
    private String id;

    @Indexed(unique = true)
    private String username;

    @JsonIgnore
    private String password;
    private Set<String> roleIds;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    @Indexed(unique = true)
    private String mail;
    private boolean mailVerify;

    private List<String> refreshTokenUUIDs;

    private String nickname;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;

    }
}
