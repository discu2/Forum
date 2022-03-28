package org.discu2.forum.account.repository;

import org.discu2.forum.common.model.Role;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@Order(HIGHEST_PRECEDENCE)
public interface RoleRepository extends MongoRepository<Role, String> {

    Optional<Role> findById(String id);

    Optional<Role> findByName(String name);

}
