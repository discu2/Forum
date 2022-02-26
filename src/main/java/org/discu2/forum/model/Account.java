package org.discu2.forum.model;

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
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

@Data
@AllArgsConstructor
@Document
public class Account{

    @Id
    private String id;

    @Indexed(unique = true)
    private String username;
    private String password;
    private Set<String> roleIds;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    @Indexed(unique = true)
    private String mail;
    private boolean mailVerify;

    private String nickname;

    public UserDetails getUserDetails() {
        return new UserDetailImpl(this);
    }

    public static class UserDetailImpl implements UserDetails {

        private static RoleRepository roleRepository = SpringContext.getBean(RoleRepository.class);
        private static AccountRepository accountRepository = SpringContext.getBean(AccountRepository.class);
        public final Account account;

        public UserDetailImpl(Account account) {
            this.account = account;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {

            var authorities = new HashSet<SimpleGrantedAuthority>();
            AtomicBoolean dirty = new AtomicBoolean(false);

            for (var id : account.getRoleIds()){
                var role = roleRepository.findById(id);

                role.ifPresentOrElse(r -> authorities.addAll(r.getGrantedAuthorities()), () -> {
                    account.getRoleIds().remove(id);
                    dirty.set(true);
                });
            }

            if (dirty.get()) accountRepository.save(account);

            return authorities;

        }

        public String getId() {
            return account.getId();
        }

        @Override
        public String getPassword() {
            return account.getPassword();
        }

        @Override
        public String getUsername() {
            return account.getUsername();
        }

        @Override
        public boolean isAccountNonExpired() {
            return account.isAccountNonExpired();
        }

        @Override
        public boolean isAccountNonLocked() {
            return account.isAccountNonLocked();
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return account.isCredentialsNonExpired();
        }

        @Override
        public boolean isEnabled() {
            return account.isEnabled();
        }
    }
}
