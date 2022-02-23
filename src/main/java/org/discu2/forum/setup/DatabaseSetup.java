package org.discu2.forum.setup;

import com.google.common.collect.Sets;
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
        if (roleRepository.findRoleByName("DEFAULT").isEmpty()) {
            var role = new Role("DEFAULT", Sets.newHashSet());
            roleRepository.insert(role);
        }
    }
}
