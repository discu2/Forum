package org.discu2.forum.repository;

import org.discu2.forum.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {

    Optional<Role> findRoleByName(String name);
}
