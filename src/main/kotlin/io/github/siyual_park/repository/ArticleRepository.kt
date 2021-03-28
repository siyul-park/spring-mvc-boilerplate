package io.github.siyual_park.repository

import io.github.siyual_park.model.article.Article
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager

@Repository
class ArticleRepository(
    entityManager: EntityManager
) : CustomRepository<Article, String> by SimpleCustomRepository.from(entityManager)
