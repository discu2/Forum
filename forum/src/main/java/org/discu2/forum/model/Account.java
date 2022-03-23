package org.discu2.forum.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.discu2.forum.repository.AccountRepository;
import org.discu2.forum.repository.RoleRepository;
import org.discu2.forum.util.SpringContext;
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

@Document
public class Account extends org.discu2.forum.api.model.Account {

    private static final RoleRepository roleRepository = SpringContext.getBean(RoleRepository.class);
    private static final AccountRepository accountRepository = SpringContext.getBean(AccountRepository.class);

    public Account(String id, String username, String password, Set<String> roleIds, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled, String mail, boolean mailVerify, List<String> refreshTokenUUIDs, String nickname) {
        super(id, username, password, roleIds, accountNonExpired, accountNonLocked, credentialsNonExpired, enabled, mail, mailVerify, refreshTokenUUIDs, nickname);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        var authorities = new HashSet<SimpleGrantedAuthority>();
        AtomicBoolean dirty = new AtomicBoolean(false);

        for (var id : super.getRoleIds()){
            var role = roleRepository.findById(id);

            role.ifPresentOrElse(r -> authorities.add(r.getGrantedAuthorities()), () -> {
                super.getRoleIds().remove(id);
                dirty.set(true);
            });
        }

        if (dirty.get()) accountRepository.save(this);

        return authorities;

    }
}
