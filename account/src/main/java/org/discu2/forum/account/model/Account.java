package org.discu2.forum.account.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.discu2.forum.account.repository.AccountRepository;
import org.discu2.forum.account.repository.RoleRepository;
import org.discu2.forum.account.util.SpringContext;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

@Data
@AllArgsConstructor
@Document
public class Account implements UserDetails {

    public static final int MAX_REFRESH_TOKENS = 6;
    private static final RoleRepository roleRepository = SpringContext.getBean(RoleRepository.class);
    private static final AccountRepository accountRepository = SpringContext.getBean(AccountRepository.class);

    @Id
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

        var authorities = new HashSet<SimpleGrantedAuthority>();
        AtomicBoolean dirty = new AtomicBoolean(false);

        for (var id : roleIds){
            var role = roleRepository.findById(id);

            role.ifPresentOrElse(r -> authorities.add(r.getGrantedAuthorities()), () -> {
                roleIds.remove(id);
                dirty.set(true);
            });
        }

        if (dirty.get()) accountRepository.save(this);

        return authorities;

    }
}