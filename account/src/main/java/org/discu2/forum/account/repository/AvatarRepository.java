package org.discu2.forum.account.repository;

import org.discu2.forum.common.model.Avatar;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AvatarRepository extends MongoRepository<Avatar, String> {

    Optional<Avatar> findByFilename(String filename);

    Optional<Avatar> findByUuid(String id);
}
