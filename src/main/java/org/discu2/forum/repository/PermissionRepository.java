package org.discu2.forum.repository;

import org.discu2.forum.model.Permission;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PermissionRepository extends MongoRepository<Permission, String> {

    Optional<Permission> findPermissionByName(String name);
}
