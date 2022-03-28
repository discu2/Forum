package org.discu2.forum.core.repository;

import org.discu2.forum.common.model.Topic;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TopicRepository extends MongoRepository<Topic, String> {

    Optional<Topic> findById(String id);

}
