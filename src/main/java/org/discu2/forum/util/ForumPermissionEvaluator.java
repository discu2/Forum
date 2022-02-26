package org.discu2.forum.util;

import lombok.AllArgsConstructor;
import org.discu2.forum.repository.RoleRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;

@AllArgsConstructor
@Configuration
public class ForumPermissionEvaluator implements PermissionEvaluator {

    //RoleRepository roleRepository;
    RoleRepository permissionRepository;

    /*
    WIP
     */

    /**
     *
     * @param authentication - 他會自己填
     * @param targetDomainObject - 板塊或身分組名稱
     * @param permission - 權限類型
     * @return
     */
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if ((authentication == null) || (targetDomainObject == null) || !(permission instanceof String)) return false;
        return hasPermission(authentication, targetDomainObject, permission);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }

    private boolean hasPermission(Authentication auth, String targetType, String permission) {

        for (var grantedAuth : auth.getAuthorities()) {
            System.out.println(grantedAuth);
            if (grantedAuth.getAuthority().startsWith(targetType) && grantedAuth.getAuthority().contains(permission))
                return true;
        }

        return false;
    }
}
