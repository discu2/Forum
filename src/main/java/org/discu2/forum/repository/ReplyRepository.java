package org.discu2.forum.repository;

import org.discu2.forum.model.TextBlock;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReplyRepository extends MongoRepository<TextBlock.Reply, String> {
}