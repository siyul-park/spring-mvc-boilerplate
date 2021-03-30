package io.github.siyual_park.repository

import io.github.siyual_park.model.category.Category
import io.github.siyual_park.repository.base.SimpleCachedByIdRepository
import org.springframework.stereotype.Component

@Component
class CachedCategoryRepository(
    repository: CategoryRepository
) : SimpleCachedByIdRepository<Category, String>(repository)
