package org.discu2.forum.setup;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import org.discu2.forum.model.Account;
import org.discu2.forum.model.Board;
import org.discu2.forum.model.Role;
import org.discu2.forum.repository.AccountRepository;
import org.discu2.forum.repository.BoardRepository;
import org.discu2.forum.repository.RoleRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DatabaseSetup {

    private final RoleRepository roleRepository;
    private final BoardRepository boardRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public void initDefaults() {

        if (roleRepository.findByName("DEFAULT").isEmpty())
            roleRepository.save(new Role(null, "DEFAULT"));

        if (roleRepository.findByName("ADMIN").isEmpty())
            roleRepository.save(new Role(null, "ADMIN"));

        if (accountRepository.findAccountByUsername("admin").isEmpty())
            accountRepository.save(new Account(
                    null,
                    "admin",
                    passwordEncoder.encode("admin"),
                    Sets.newHashSet(roleRepository.findByName("ADMIN").get().getId()),
                    true,
                    true,
                    true,
                    true,
                    "admin@mail.com",
                    false,
                    "admin"
            ));

        if (boardRepository.findAll().isEmpty())
            boardRepository.save(Board.createNewBoard("default", "Default"));
    }
}
