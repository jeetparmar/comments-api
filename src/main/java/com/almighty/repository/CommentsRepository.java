package com.almighty.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.almighty.domain.Comments;

import java.util.List;

@Repository
public interface CommentsRepository extends MongoRepository<Comments, String> {

	@Query("{parentId: ?0}")
	Page<Comments> findAllByParentId(String parentId, Pageable pageable);

	List<Comments> findAllByParentId(String parentId);

	Page<Comments> findAllByParentIdIsNull(Pageable pageable);

	Page<Comments> findAllByParentIdIsNullAndTextContainingIgnoreCase(String text, Pageable pageable);

	Page<Comments> findAllByParentIdAndTextContainingIgnoreCase(String parentId, String text, Pageable pageable);
}
