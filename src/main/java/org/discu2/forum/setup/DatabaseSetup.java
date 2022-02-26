package org.discu2.forum.setup;

import lombok.AllArgsConstructor;
import org.discu2.forum.model.Role;
import org.discu2.forum.repository.RoleRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DatabaseSetup {

    private final RoleRepository roleRepository;

    @Bean
    public void initRoles() {

        if (roleRepository.findByName("DEFAULT").isEmpty())
            roleRepository.save(new Role(null, "DEFAULT"));

    }
}
