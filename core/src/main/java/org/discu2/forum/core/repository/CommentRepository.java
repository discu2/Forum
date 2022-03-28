package org.discu2.forum.core.repository;

import org.discu2.forum.common.model.TextBlock;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<TextBlock.Comment, String> {

    List<TextBlock.Comment> findByPostId(String masterId);
}

