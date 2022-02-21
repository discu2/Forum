package org.discu2.forum.data;

import org.discu2.forum.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountRepository extends MongoRepository<Account, String> {

}
