package org.discu2.forum.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

@Data
public class Account implements UserDetails {

    @Id
    private String id;

    @Indexed(unique = true)
    private String username;
    private String password;
    private Set<? extends GrantedAuthority> authorities;
    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private boolean isEnabled;

    @Indexed(unique = true)
    private String mail;
    private boolean mailVerify;

    public Account(String username, String password, Set<? extends GrantedAuthority> authorities, boolean isAccountNonExpired, boolean isAccountNonLocked, boolean isCredentialsNonExpired, boolean isEnabled, String mail, boolean mailVerify) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.isAccountNonExpired = isAccountNonExpired;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isCredentialsNonExpired = isCredentialsNonExpired;
        this.isEnabled = isEnabled;
        this.mail = mail;
        this.mailVerify = mailVerify;
    }
}
