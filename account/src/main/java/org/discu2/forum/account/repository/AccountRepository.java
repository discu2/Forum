package org.discu2.forum.account.repository;

import org.discu2.forum.account.model.Account;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@Order(HIGHEST_PRECEDENCE)
public interface AccountRepository extends MongoRepository<Account, String> {

    Optional<Account> findAccountByMail(String mail);

    Optional<Account> findAccountByUsername (String name);
}
