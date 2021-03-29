package io.github.siyual_park.repository

import io.github.siyual_park.model.category.Category
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager

@Repository
class CategoryRepository(
    entityManager: EntityManager
) : CustomRepository<Category, String> by SimpleCustomRepository.from(entityManager)
