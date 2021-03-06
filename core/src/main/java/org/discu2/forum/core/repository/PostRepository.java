package org.discu2.forum.core.repository;

import org.discu2.forum.common.model.TextBlock;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PostRepository extends MongoRepository<TextBlock.Post, String> {

    Optional<TextBlock.Post> findById(String id);

    Optional<TextBlock.Post> findByTopicIdAndOriginPostIsTrue(String topic);

}
