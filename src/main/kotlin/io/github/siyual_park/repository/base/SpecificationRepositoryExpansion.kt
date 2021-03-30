package io.github.siyual_park.repository.base

import io.github.siyual_park.repository.patch.Patch
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.transaction.annotation.Transactional
import javax.persistence.LockModeType

@NoRepositoryBean
interface SpecificationRepositoryExpansion<T : Any> {
    @Transactional
    fun update(spec: Specification<T>, patch: Patch<T>): T?

    @Transactional
    fun findOrFail(spec: Specification<T>, lockMode: LockModeType? = null): T

    @Transactional
    fun find(spec: Specification<T>, lockMode: LockModeType? = null): T?

    @Transactional
    fun findAll(spec: Specification<T>, sort: Sort? = null): List<T>

    @Transactional
    fun count(spec: Specification<T>): Long

    @Transactional
    fun delete(spec: Specification<T>)
}
