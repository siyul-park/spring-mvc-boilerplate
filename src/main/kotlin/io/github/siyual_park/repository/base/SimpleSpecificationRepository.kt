package io.github.siyual_park.repository.base

import org.springframework.data.repository.NoRepositoryBean
import javax.persistence.EntityManager
import kotlin.reflect.KClass

@NoRepositoryBean
open class SimpleSpecificationRepository<T : Any, ID>(
    crudRepository: CrudRepository<T, ID>,
    clazz: KClass<T>,
    entityManager: EntityManager
) : SpecificationRepository<T, ID>,
    CrudRepository<T, ID> by crudRepository,
    SpecificationRepositoryExpansion<T> by SimpleSpecificationRepositoryExpansion(
        crudRepository,
        clazz,
        entityManager
    ) {
    companion object {
        inline fun <reified T : Any, ID> from(
            entityManager: EntityManager
        ) = SimpleSpecificationRepository<T, ID>(
            SimpleCrudRepository(T::class, entityManager),
            T::class,
            entityManager
        )
    }
}
