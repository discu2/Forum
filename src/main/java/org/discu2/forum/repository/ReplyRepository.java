package org.discu2.forum.repository;

import org.discu2.forum.model.TextBlock;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ReplyRepository extends MongoRepository<TextBlock.Reply, String> {

    Optional<TextBlock.Reply> findById(String id);

    List<TextBlock.Reply> findByTopicId(String topicId);

}
