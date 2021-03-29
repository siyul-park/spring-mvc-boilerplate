package io.github.siyual_park.repository

import io.github.siyual_park.exception.NotFoundException
import io.github.siyual_park.model.category.Category
import io.github.siyual_park.repository.specification.CategorySpecification
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.LockModeType
import javax.transaction.Transactional

@Repository
class CategoryRepository(
    entityManager: EntityManager
) : CustomRepository<Category, String> by SimpleCustomRepository.from(entityManager) {
    @Transactional
    fun findByNameOrFail(name: String, lockMode: LockModeType? = null): Category = findByName(name, lockMode) ?: throw NotFoundException()

    @Transactional
    fun findByName(name: String, lockMode: LockModeType? = null): Category? = find(CategorySpecification.withName(name), lockMode)
}
