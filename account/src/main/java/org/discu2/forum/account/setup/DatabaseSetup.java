package org.discu2.forum.account.setup;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import org.discu2.forum.account.repository.RoleRepository;
import org.discu2.forum.account.service.AccountService;
import org.discu2.forum.common.exception.AlreadyExistException;
import org.discu2.forum.common.exception.BadPacketFormatException;
import org.discu2.forum.common.exception.DataNotFoundException;
import org.discu2.forum.common.model.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class DatabaseSetup {

    private final AccountService accountService;
    private final RoleRepository roleRepository;

    @Bean
    public void initDefaults() throws AlreadyExistException, DataNotFoundException, BadPacketFormatException {

        var roles = Sets.newHashSet("ANONYMOUS", "DEFAULT", "ADMIN");

        for (var id : roles)
            if (roleRepository.findByName(id).isEmpty()) {
                var role = new Role(id, id);
                roleRepository.insert(role);
            }

        try {
            accountService.loadUserByUsername("admin");
        } catch (Exception e) {
            accountService.registerNewAccount("admin", "adminpass1", "ADMIN", "admin@mail.com", null);
        }
    }
}
