package io.github.siyual_park.repository

import io.github.siyual_park.repository.patch.Patch
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.Repository

@NoRepositoryBean
interface CustomRepository<T : Any, ID> : Repository<T, ID> {
    fun <S : T> save(entity: S): S

    fun <S : T> saveAll(entities: Iterable<S>): Iterable<S>

    fun findById(id: ID): T?

    fun existsById(id: ID): Boolean

    fun findAll(): Iterable<T>

    fun findAllById(ids: Iterable<ID>): Iterable<T>

    fun count(): Long

    fun deleteById(id: ID)

    fun delete(entity: T)

    fun deleteAll(entities: Iterable<T>)

    fun deleteAll()

    fun updateById(id: ID, patch: Patch<T>): T?
}
