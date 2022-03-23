package org.discu2.forum.repository;

import org.discu2.forum.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AccountRepository extends MongoRepository<Account, String> {

    Optional<Account> findAccountByMail(String mail);

    Optional<Account> findAccountByUsername (String name);
}
