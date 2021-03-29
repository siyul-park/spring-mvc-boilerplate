package io.github.siyual_park.repository

import io.github.siyual_park.repository.patch.Patch
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.Repository
import org.springframework.transaction.annotation.Transactional

@NoRepositoryBean
interface CustomRepository<T : Any, ID> : Repository<T, ID> {
    @Transactional
    fun <S : T> save(entity: S): S

    @Transactional
    fun <S : T> saveAll(entities: Iterable<S>): Iterable<S>
    @Transactional

    fun findById(id: ID): T?
    @Transactional

    fun findByIdOrFail(id: ID): T
    @Transactional

    fun existsById(id: ID): Boolean
    @Transactional

    fun findAll(): Iterable<T>
    @Transactional

    fun findAllById(ids: Iterable<ID>): Iterable<T>
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

    @Transactional
    fun updateById(id: ID, patch: Patch<T>): T?

    @Transactional
    fun updateByIdOrFail(id: ID, patch: Patch<T>): T
}
