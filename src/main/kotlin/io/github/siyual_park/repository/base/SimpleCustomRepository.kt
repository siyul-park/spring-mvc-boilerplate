package io.github.siyual_park.repository.base

import javax.persistence.EntityManager
import kotlin.reflect.KClass

open class SimpleCustomRepository<T : Any, ID, FACTORY>(
    repository: CrudRepository<T, ID>,
    clazz: KClass<T>,
    entityManager: EntityManager,
    factory: FACTORY
) : CustomRepository<T, ID, FACTORY>,
    CrudRepository<T, ID> by repository,
    SpecificationRepositoryExpansion<T> by SimpleSpecificationRepositoryExpansion(
        repository,
        clazz,
        entityManager,
    ),
    PaginationRepositoryExpansion<T> by SimplePaginationRepositoryExpansion(
        repository,
        clazz,
        entityManager
    ),
    SpecificationDSLRepositoryExpansion<T, FACTORY> by SimpleSpecificationDSLRepositoryExpansion(
        SimpleSpecificationRepository(repository, clazz, entityManager),
        clazz,
        entityManager,
        factory
    ),
    SpecificationPaginationRepositoryExpansion<T> by SimpleSpecificationPaginationRepositoryExpansion(
        repository,
        clazz,
        entityManager
    ) {
    companion object {
        inline fun <reified T : Any, ID, FACTORY> of(
            entityManager: EntityManager,
            factory: FACTORY
        ) = SimpleCustomRepository<T, ID, FACTORY>(
            SimpleCrudRepository(T::class, entityManager),
            T::class,
            entityManager,
            factory
        )
    }
}
