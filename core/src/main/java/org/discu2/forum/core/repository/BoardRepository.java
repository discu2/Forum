package org.discu2.forum.core.repository;

import org.discu2.forum.common.model.Board;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends MongoRepository<Board, String> {

    Optional<Board> findById(String id);

    List<Board> findByGroupName(String groupName);

    Optional<Board> findByGroupNameAndAndName(String groupName, String name);
}
