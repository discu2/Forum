package org.discu2.forum.setup;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import org.discu2.forum.model.Permission;
import org.discu2.forum.model.Role;
import org.discu2.forum.repository.PermissionRepository;
import org.discu2.forum.repository.RoleRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DatabaseSetup {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;

    @Bean
    public void initRoles() {
        var permissions = new Permission[] {
                new Permission(null, "account_self"),
                new Permission(null, "post_DEFAULT"),
                new Permission(null, "reply_DEFAULT"),
                new Permission(null, "comment_DEFAULT")
        };

        for (var permission : permissions) {
            if (permissionRepository.findPermissionByName(permission.getName()).isEmpty())
                permissionRepository.save(permission);
        }

        if (roleRepository.findRoleByName("DEFAULT").isEmpty()) {
            var role = new Role(null, "DEFAULT", Sets.newHashSet(permissions));
            roleRepository.save(role);
        }
    }
}
