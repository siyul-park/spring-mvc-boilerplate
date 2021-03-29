package io.github.siyual_park.repository

import io.github.siyual_park.exception.ConflictException
import io.github.siyual_park.exception.NotFoundException
import io.github.siyual_park.repository.patch.Patch
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport
import org.springframework.data.jpa.repository.support.SimpleJpaRepository
import org.springframework.data.repository.NoRepositoryBean
import javax.persistence.EntityManager
import javax.persistence.LockModeType
import kotlin.reflect.KClass

@NoRepositoryBean
class SimpleCustomRepository<T : Any, ID>(
    private val clazz: KClass<T>,
    private val entityManager: EntityManager
) : CustomRepository<T, ID> {

    private val entityInformation = JpaEntityInformationSupport.getEntityInformation(clazz.java, entityManager)

    // TODO(제거 하기)
    private val simpleJpaRepository = SimpleJpaRepository<T, ID>(clazz.java, entityManager)

    override fun <S : T> create(entity: S): S = if (entityInformation.isNew(entity)) {
        entityManager.persist(entity)
        entity
    } else {
        throw ConflictException()
    }

    override fun <S : T> createAll(entities: Iterable<S>): Iterable<S> = mutableListOf<S>().apply {
        for (entity in entities) {
            add(create(entity))
        }
    }

    override fun updateByIdOrFail(id: ID, patch: Patch<T>): T = updateById(id, patch) ?: throw NotFoundException()

    override fun updateById(id: ID, patch: Patch<T>): T? = findById(id, LockModeType.PESSIMISTIC_WRITE)?.let { update(it, patch) }

    override fun update(entity: T, patch: Patch<T>): T = if (!entityInformation.isNew(entity)) {
        entityManager.merge(patch.apply(entity))
    } else {
        throw NotFoundException()
    }

    override fun <S : T> upsert(entity: S): S = if (entityInformation.isNew(entity)) {
        entityManager.persist(entity)
        entity
    } else {
        entityManager.merge(entity)
    }

    override fun <S : T> upsertAll(entities: Iterable<S>): Iterable<S> = mutableListOf<S>().apply {
        for (entity in entities) {
            add(upsert(entity))
        }
    }

    override fun findByIdOrFail(id: ID, lockMode: LockModeType?): T = findById(id, lockMode) ?: throw NotFoundException()

    override fun findById(id: ID, lockMode: LockModeType?): T? = warpException { entityManager.find(clazz.java, id, lockMode) }

    override fun findOrFail(spec: Specification<T>, lockMode: LockModeType?): T = find(spec, lockMode)
        ?: throw NotFoundException()

    override fun find(spec: Specification<T>, lockMode: LockModeType?): T? = warpException { simpleJpaRepository.findOne(spec) }
        .let {
            when (it.isPresent) {
                true -> it.get()
                false -> null
            }
        }
        ?.also { entityManager.lock(it, lockMode) }

    override fun existsById(id: ID): Boolean = warpException { simpleJpaRepository.existsById(id!!) }

    override fun findAll(pageable: Pageable): Page<T> = warpException { simpleJpaRepository.findAll(pageable) }

    override fun findAll(): Iterable<T> = warpException { simpleJpaRepository.findAll() }

    override fun findAllById(ids: Iterable<ID>): Iterable<T> = warpException { simpleJpaRepository.findAllById(ids) }

    override fun findAll(spec: Specification<T>): Iterable<T> = warpException { simpleJpaRepository.findAll(spec) }

    override fun count(): Long = warpException { simpleJpaRepository.count() }

    override fun deleteByIdOrFail(id: ID): Unit = delete(findByIdOrFail(id))

    override fun deleteById(id: ID): Unit = findById(id)?.let { delete(it) } ?: Unit

    override fun delete(entity: T): Unit = warpException { simpleJpaRepository.delete(entity) }

    override fun deleteAll(entities: Iterable<T>): Unit = warpException { simpleJpaRepository.deleteAll(entities) }

    override fun deleteAll(): Unit = warpException { simpleJpaRepository.deleteAll() }

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
