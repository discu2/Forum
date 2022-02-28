package org.discu2.forum.repository;

import org.discu2.forum.model.TextBlock;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<TextBlock.Comment, String> {

    List<TextBlock.Comment> findByMasterTypeAndMasterId(String masterType, String masterId);
}

