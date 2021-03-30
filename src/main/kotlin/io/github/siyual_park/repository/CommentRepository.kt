package io.github.siyual_park.repository

import io.github.siyual_park.model.comment.Comment
import io.github.siyual_park.repository.specification.CommentSpecification
import org.springframework.stereotype.Component
import javax.persistence.EntityManager

@Component
class CommentRepository(
    entityManager: EntityManager
) : CustomRepository<Comment, String, CommentSpecification> by SimpleCustomRepository.of(entityManager, CommentSpecification)
