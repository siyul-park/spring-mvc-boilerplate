package io.github.siyual_park.repository

import io.github.siyual_park.repository.patch.Patch
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.transaction.annotation.Transactional
import javax.persistence.LockModeType

@NoRepositoryBean
interface SpecificationDSLRepositoryExpansion<T : Any, FACTORY> {
    @Transactional
    fun update(spec: FACTORY.() -> Specification<T>, patch: Patch<T>): T?

    @Transactional
    fun findOrFail(spec: FACTORY.() -> Specification<T>, lockMode: LockModeType? = null): T

    @Transactional
    fun find(spec: FACTORY.() -> Specification<T>, lockMode: LockModeType? = null): T?

    @Transactional
    fun findAll(spec: FACTORY.() -> Specification<T>, sort: Sort? = null): List<T>

    @Transactional
    fun count(spec: FACTORY.() -> Specification<T>): Long

    @Transactional
    fun delete(spec: FACTORY.() -> Specification<T>)
}
