package io.github.siyual_park.repository

import io.github.siyual_park.model.article.Article
import io.github.siyual_park.repository.base.CustomRepository
import io.github.siyual_park.repository.base.SimpleCustomRepository
import io.github.siyual_park.repository.specification.ArticleSpecification
import org.springframework.stereotype.Component
import javax.persistence.EntityManager

@Component
class ArticleRepository(
    entityManager: EntityManager
) : CustomRepository<Article, String, ArticleSpecification> by SimpleCustomRepository.of(entityManager, ArticleSpecification)
