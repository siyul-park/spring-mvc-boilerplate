package io.github.siyual_park.repository.base

import org.springframework.data.repository.NoRepositoryBean
import javax.persistence.EntityManager
import kotlin.reflect.KClass

@NoRepositoryBean
open class SimpleSpecificationDSLRepository<T : Any, ID, FACTORY>(
    repository: SpecificationRepository<T, ID>,
    clazz: KClass<T>,
    entityManager: EntityManager,
    factory: FACTORY
) : SpecificationDSLRepository<T, ID, FACTORY>,
    SpecificationRepository<T, ID> by repository,
    SpecificationDSLRepositoryExpansion<T, FACTORY> by SimpleSpecificationDSLRepositoryExpansion(
        repository,
        clazz,
        entityManager,
        factory
    ) {
    companion object {
        inline fun <reified T : Any, ID, FACTORY> of(
            entityManager: EntityManager,
            factory: FACTORY
        ) = SimpleSpecificationDSLRepository<T, ID, FACTORY>(
            SimpleSpecificationRepository(
                SimpleCrudRepository(T::class, entityManager),
                T::class,
                entityManager
            ),
            T::class,
            entityManager,
            factory
        )
    }
}
