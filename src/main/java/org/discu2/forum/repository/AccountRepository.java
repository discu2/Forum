package org.discu2.forum.repository;

import org.discu2.forum.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AccountRepository extends MongoRepository<Account, String> {

    Optional<Account> findAccountsByMail(String mail);

    Optional<Account> findAccountsByName(String name);
}
