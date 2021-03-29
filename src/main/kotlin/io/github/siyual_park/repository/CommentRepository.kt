package io.github.siyual_park.repository

import io.github.siyual_park.model.comment.Comment
import io.github.siyual_park.repository.specification.CommentSpecification
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager

@Repository
class CommentRepository(
    entityManager: EntityManager
) : CustomRepository<Comment, String, CommentSpecification> by SimpleCustomRepository.of(entityManager, CommentSpecification)
