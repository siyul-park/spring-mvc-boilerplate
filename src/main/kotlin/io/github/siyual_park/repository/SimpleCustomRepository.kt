package io.github.siyual_park.repository

import io.github.siyual_park.exception.NotFoundException
import io.github.siyual_park.repository.patch.Patch
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.jpa.repository.support.SimpleJpaRepository
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.LockModeType
import kotlin.reflect.KClass

@Suppress("NULLABLE_TYPE_PARAMETER_AGAINST_NOT_NULL_TYPE_PARAMETER")
@NoRepositoryBean
open class SimpleCustomRepository<T : Any, ID>(
    private val clazz: KClass<T>,
    private val entityManager: EntityManager
) : CustomRepository<T, ID> {

    private val simpleJpaRepository = SimpleJpaRepository<T, ID>(clazz.java, entityManager)

    override fun <S : T> save(entity: S): S = warpException { simpleJpaRepository.save(entity) }

    override fun <S : T> saveAll(entities: Iterable<S>): Iterable<S> = warpException { simpleJpaRepository.saveAll(entities) }

    override fun findById(id: ID): T? = warpException { simpleJpaRepository.findByIdOrNull(id) }

    override fun findByIdOrFail(id: ID): T = findById(id) ?: throw NotFoundException()

    override fun existsById(id: ID): Boolean = warpException { simpleJpaRepository.existsById(id) }

    override fun findAll(): Iterable<T> = warpException { simpleJpaRepository.findAll() }

    override fun findAllById(ids: Iterable<ID>): Iterable<T> = warpException { simpleJpaRepository.findAllById(ids) }

    override fun count(): Long = warpException { simpleJpaRepository.count() }

    @Transactional
    override fun deleteById(id: ID): Unit = findById(id)?.let { delete(it) } ?: Unit

    override fun delete(entity: T): Unit = warpException { simpleJpaRepository.delete(entity) }

    override fun deleteAll(entities: Iterable<T>): Unit = warpException { simpleJpaRepository.deleteAll(entities) }

    override fun deleteAll(): Unit = warpException { simpleJpaRepository.deleteAll() }

    @Transactional
    override fun updateById(id: ID, patch: Patch<T>): T? = warpException { entityManager.find(clazz.java, id, LockModeType.PESSIMISTIC_WRITE) }
        ?.let { patch.apply(it) }
        ?.let { save(it) }

    override fun updateByIdOrFail(id: ID, patch: Patch<T>): T = updateById(id, patch) ?: throw NotFoundException()

    private inline fun <R> warpException(function: () -> R): R {
        try {
            return function()
        } catch (e: EmptyResultDataAccessException) {
            throw NotFoundException(e.message)
        }
    }

    companion object {
        inline fun <reified T : Any, ID> from(entityManager: EntityManager) = SimpleCustomRepository<T, ID>(
            T::class,
            entityManager
        )
    }
}
