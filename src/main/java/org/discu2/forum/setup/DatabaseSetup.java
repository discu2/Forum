package org.discu2.forum.setup;

import lombok.AllArgsConstructor;
import org.discu2.forum.exception.AlreadyExistException;
import org.discu2.forum.exception.DataNotFoundException;
import org.discu2.forum.model.Role;
import org.discu2.forum.repository.BoardRepository;
import org.discu2.forum.service.AccountService;
import org.discu2.forum.service.BoardService;
import org.discu2.forum.service.RoleService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DatabaseSetup {

    private final RoleService roleService;
    private final BoardService boardService;
    private final BoardRepository boardRepository;
    private final AccountService accountService;

    @Bean
    public void initDefaults() throws AlreadyExistException, DataNotFoundException {

        Role adminRole;

        try {
            roleService.loadRoleByName("DEFAULT");
        } catch (Exception e) {
            roleService.createNewRole("DEFAULT");
        }

        try {
            adminRole = roleService.loadRoleByName("ADMIN");
        } catch (Exception e) {
            adminRole = roleService.createNewRole("ADMIN");
        }

        try {
            accountService.loadUserByUsername("admin");
        } catch (Exception e) {
            accountService.registerNewAccount("admin", "admin", adminRole.getId(), "admin@mail.com", null);
        }

        if (boardRepository.findAll().isEmpty())
            boardService.createNewBoard("Default Group", "Default Board", "DEFAULT");
    }
}
