package io.github.siyual_park.repository

import io.github.siyual_park.repository.patch.Patch
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.transaction.annotation.Transactional
import javax.persistence.LockModeType

@NoRepositoryBean
interface CustomRepository<T : Any, ID> {
    @Transactional
    fun <S : T> create(entity: S): S

    @Transactional
    fun <S : T> createAll(entities: Iterable<S>): Iterable<S>

    @Transactional
    fun updateByIdOrFail(id: ID, patch: Patch<T>): T

    @Transactional
    fun updateById(id: ID, patch: Patch<T>): T?

    @Transactional
    fun update(entity: T, patch: Patch<T>): T

    @Transactional
    fun <S : T> upsert(entity: S): S

    @Transactional
    fun <S : T> upsertAll(entities: Iterable<S>): Iterable<S>

    @Transactional
    fun findByIdOrFail(id: ID, lockMode: LockModeType? = null): T

    @Transactional
    fun findById(id: ID, lockMode: LockModeType? = null): T?

    @Transactional
    fun findOrFail(spec: Specification<T>, lockMode: LockModeType? = null): T

    @Transactional
    fun find(spec: Specification<T>, lockMode: LockModeType? = null): T?

    @Transactional
    fun existsById(id: ID): Boolean

    @Transactional
    fun findAll(spec: Specification<T>, pageable: Pageable): Page<T>

    @Transactional
    fun findAll(pageable: Pageable): Page<T>

    @Transactional
    fun findAll(): Iterable<T>

    @Transactional
    fun findAllById(ids: Iterable<ID>): Iterable<T>

    @Transactional
    fun findAll(spec: Specification<T>): Iterable<T>

    @Transactional
    fun count(): Long

    @Transactional
    fun deleteByIdOrFail(id: ID)

    @Transactional
    fun deleteById(id: ID)

    @Transactional
    fun delete(entity: T)

    @Transactional
    fun deleteAll(entities: Iterable<T>)

    @Transactional
    fun deleteAll()
}
