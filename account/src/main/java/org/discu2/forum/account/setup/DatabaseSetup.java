package org.discu2.forum.account.setup;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import org.discu2.forum.account.repository.AccountRepository;
import org.discu2.forum.account.repository.RoleRepository;
import org.discu2.forum.account.service.AccountService;
import org.discu2.forum.account.service.RoleService;
import org.discu2.forum.api.exception.AlreadyExistException;
import org.discu2.forum.api.exception.BadPacketFormatException;
import org.discu2.forum.api.exception.DataNotFoundException;
import org.discu2.forum.api.model.Role;
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
