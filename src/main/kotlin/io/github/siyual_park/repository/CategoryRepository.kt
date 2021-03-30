package io.github.siyual_park.repository

import io.github.siyual_park.model.category.Category
import io.github.siyual_park.repository.specification.CategorySpecification
import org.springframework.stereotype.Component
import javax.persistence.EntityManager
import javax.persistence.LockModeType
import javax.transaction.Transactional

@Component
class CategoryRepository(
    entityManager: EntityManager
) : CustomRepository<Category, String, CategorySpecification> by SimpleCustomRepository.of(entityManager, CategorySpecification) {
    @Transactional
    fun findByNameOrFail(name: String, lockMode: LockModeType? = null): Category = findOrFail({ withName(name) }, lockMode)

    @Transactional
    fun findByName(name: String, lockMode: LockModeType? = null): Category? = find({ withName(name) }, lockMode)
}
