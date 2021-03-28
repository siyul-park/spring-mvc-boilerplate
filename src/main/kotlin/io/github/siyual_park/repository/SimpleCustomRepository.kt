package io.github.siyual_park.repository

import io.github.siyual_park.repository.patch.Patch
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

    override fun <S : T> save(entity: S): S = simpleJpaRepository.save(entity)

    override fun <S : T> saveAll(entities: Iterable<S>): Iterable<S> = simpleJpaRepository.saveAll(entities)

    override fun findById(id: ID): T? = simpleJpaRepository.findByIdOrNull(id)

    override fun existsById(id: ID): Boolean = simpleJpaRepository.existsById(id)

    override fun findAll(): Iterable<T> = simpleJpaRepository.findAll()

    override fun findAllById(ids: Iterable<ID>): Iterable<T> = simpleJpaRepository.findAllById(ids)

    override fun count(): Long = simpleJpaRepository.count()

    override fun deleteById(id: ID) = simpleJpaRepository.deleteById(id)

    override fun delete(entity: T) = simpleJpaRepository.delete(entity)

    override fun deleteAll(entities: Iterable<T>) = simpleJpaRepository.deleteAll(entities)

    override fun deleteAll() = simpleJpaRepository.deleteAll()

    @Transactional
    override fun updateById(id: ID, patch: Patch<T>): T = entityManager.find(clazz.java, id, LockModeType.PESSIMISTIC_WRITE)
        .let { patch.apply(it) }
        .let { save(it) }

    companion object {
        inline fun <reified T : Any, ID> from(entityManager: EntityManager) = SimpleCustomRepository<T, ID>(
            T::class,
            entityManager
        )
    }
}
