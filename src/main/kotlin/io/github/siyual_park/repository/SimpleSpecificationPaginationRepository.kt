package io.github.siyual_park.repository

import org.springframework.data.repository.NoRepositoryBean
import javax.persistence.EntityManager
import kotlin.reflect.KClass

@NoRepositoryBean
open class SimpleSpecificationPaginationRepository<T : Any, ID>(
    crudRepository: CrudRepository<T, ID>,
    clazz: KClass<T>,
    entityManager: EntityManager
) : SpecificationPaginationRepository<T, ID>,
    CrudRepository<T, ID> by crudRepository,
    SpecificationRepositoryExpansion<T> by SimpleSpecificationRepositoryExpansion(
        crudRepository,
        clazz,
        entityManager
    ),
    PaginationRepositoryExpansion<T> by SimplePaginationRepositoryExpansion(
        crudRepository,
        clazz,
        entityManager
    ),
    SpecificationPaginationRepositoryExpansion<T> by SimpleSpecificationPaginationRepositoryExpansion(
        crudRepository,
        clazz,
        entityManager
    ) {
    companion object {
        inline fun <reified T : Any, ID> from(
            entityManager: EntityManager
        ) = SimpleSpecificationPaginationRepository<T, ID>(
            SimpleCrudRepository(T::class, entityManager),
            T::class,
            entityManager
        )
    }
}
