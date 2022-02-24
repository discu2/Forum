package org.discu2.forum.repository;

import org.discu2.forum.model.Topic;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TopicRepository extends MongoRepository<Topic, String> {

}
