package org.discu2.forum.account.repository;

import org.discu2.forum.account.model.ProfilePic;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProfilePicRepository extends MongoRepository<ProfilePic, String> {

    Optional<ProfilePic> findByUsername(String username);
}
