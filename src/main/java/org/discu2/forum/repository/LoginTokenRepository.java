package org.discu2.forum.repository;

import org.discu2.forum.model.LoginToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface LoginTokenRepository extends MongoRepository<LoginToken, String> {

    Optional<LoginToken> findBySeries(String series);

    Optional<LoginToken> findByUsername(String username);
}
