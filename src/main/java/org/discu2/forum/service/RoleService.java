package org.discu2.forum.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.discu2.forum.exception.AlreadyExistException;
import org.discu2.forum.exception.DataNotFoundException;
import org.discu2.forum.model.Role;
import org.discu2.forum.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;


    public Role createNewRole(@NonNull String name) throws AlreadyExistException {

        var role = new Role(null, name.toUpperCase());

        try {
            return roleRepository.save(role);
        } catch (Exception e) {
            throw new AlreadyExistException(Role.class);
        }

    }

    public Role loadRoleByName(@NonNull String name) throws DataNotFoundException {
        return roleRepository.findByName(name.toUpperCase()).orElseThrow(() -> new DataNotFoundException(Role.class, "name", name));
    }
}
