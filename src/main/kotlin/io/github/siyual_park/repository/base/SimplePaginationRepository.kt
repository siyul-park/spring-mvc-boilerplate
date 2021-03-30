package io.github.siyual_park.repository.base

import org.springframework.data.repository.NoRepositoryBean
import javax.persistence.EntityManager
import kotlin.reflect.KClass

@NoRepositoryBean
open class SimplePaginationRepository<T : Any, ID> (
    crudRepository: CrudRepository<T, ID>,
    clazz: KClass<T>,
    entityManager: EntityManager
) : PaginationRepository<T, ID>,
    CrudRepository<T, ID> by crudRepository,
    PaginationRepositoryExpansion<T> by SimplePaginationRepositoryExpansion(
        crudRepository,
        clazz,
        entityManager
    ) {
    companion object {
        inline fun <reified T : Any, ID> from(
            entityManager: EntityManager
        ) = SimplePaginationRepository<T, ID>(
            SimpleCrudRepository(T::class, entityManager),
            T::class,
            entityManager
        )
    }
}
