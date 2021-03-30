package io.github.siyual_park.repository.base

import io.github.siyual_park.repository.patch.Patch
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.repository.NoRepositoryBean
import javax.persistence.EntityManager
import javax.persistence.LockModeType
import kotlin.reflect.KClass

@NoRepositoryBean
open class SimpleSpecificationDSLRepositoryExpansion<T : Any, ID, FACTORY>(
    val repository: SpecificationRepository<T, ID>,
    val clazz: KClass<T>,
    val entityManager: EntityManager,
    val factory: FACTORY
) : SpecificationDSLRepositoryExpansion<T, FACTORY> {
    override fun update(spec: FACTORY.() -> Specification<T>, patch: Patch<T>): T {
        return repository.update(spec(factory), patch)
    }

    override fun findOrFail(spec: FACTORY.() -> Specification<T>, lockMode: LockModeType?): T {
        return repository.findOrFail(spec(factory), lockMode)
    }

    override fun find(spec: FACTORY.() -> Specification<T>, lockMode: LockModeType?): T? {
        return repository.findOrFail(spec(factory), lockMode)
    }

    override fun findAll(spec: FACTORY.() -> Specification<T>, sort: Sort?): List<T> {
        return repository.findAll(spec(factory), sort)
    }

    override fun count(spec: FACTORY.() -> Specification<T>): Long {
        return repository.count(spec(factory))
    }

    override fun delete(spec: FACTORY.() -> Specification<T>) {
        return repository.delete(spec(factory))
    }
}
