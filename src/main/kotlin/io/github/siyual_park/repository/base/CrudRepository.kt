package io.github.siyual_park.repository.base

import io.github.siyual_park.repository.patch.Patch
import org.springframework.data.domain.Sort
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.LockModeType
import kotlin.reflect.KClass

@NoRepositoryBean
interface CrudRepository<T : Any, ID> {
    val clazz: KClass<T>
    val entityManager: EntityManager

    @Transactional
    fun <S : T> create(entity: S): S

    @Transactional
    fun <S : T> createAll(entities: Iterable<S>): List<S>

    @Transactional
    fun updateById(id: ID, patch: Patch<T>): T

    @Transactional
    fun update(entity: T, patch: Patch<T>): T

    @Transactional
    fun <S : T> upsert(entity: S): S

    @Transactional
    fun <S : T> upsertAll(entities: Iterable<S>): List<S>

    @Transactional
    fun findByIdOrFail(id: ID, lockMode: LockModeType? = null): T

    @Transactional
    fun findById(id: ID, lockMode: LockModeType? = null): T?

    @Transactional
    fun existsById(id: ID): Boolean

    @Transactional
    fun findAll(sort: Sort? = null, lockMode: LockModeType? = null): List<T>

    @Transactional
    fun findAllByIdIn(ids: Iterable<ID>, sort: Sort? = null): List<T>

    @Transactional
    fun count(): Long

    @Transactional
    fun deleteById(id: ID)

    @Transactional
    fun delete(entity: T)

    @Transactional
    fun deleteAll(entities: Iterable<T>)

    @Transactional
    fun deleteAll()
}
