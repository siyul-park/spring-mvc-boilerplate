package io.github.siyual_park.repository.base

import io.github.siyual_park.repository.expansion.EntityInformationProvider
import io.github.siyual_park.repository.patch.Patch
import io.github.siyual_park.repository.query.QueryManagerProvider
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.repository.NoRepositoryBean
import javax.persistence.EntityManager
import javax.persistence.EntityNotFoundException
import javax.persistence.LockModeType
import javax.persistence.NoResultException
import kotlin.reflect.KClass

@NoRepositoryBean
class SimpleSpecificationRepositoryExpansion<T : Any, ID>(
    private val crudRepository: CrudRepository<T, ID>,
    val clazz: KClass<T>,
    val entityManager: EntityManager
) : SpecificationRepositoryExpansion<T> {

    private val entityInformation = EntityInformationProvider.of(clazz, entityManager)
    private val queryManager = QueryManagerProvider.of(clazz, entityManager)

    override fun update(spec: Specification<T>, patch: Patch<T>): T =
        find(spec, LockModeType.PESSIMISTIC_WRITE)
            ?.let { crudRepository.update(it, patch) }
            ?: throw EntityNotFoundException("No ${entityInformation.javaType} entity exists!")

    override fun findOrFail(spec: Specification<T>, lockMode: LockModeType?): T = find(spec, lockMode) ?: throw EntityNotFoundException(
        "No ${entityInformation.javaType} entity exists!"
    )

    override fun find(spec: Specification<T>, lockMode: LockModeType?): T? {
        return try {
            queryManager.getQuery(spec, Sort.unsorted()).singleResult
                .apply { if (lockMode != null) entityManager.lock(this, lockMode) }
        } catch (e: NoResultException) {
            null
        }
    }

    override fun findAll(spec: Specification<T>, lockMode: LockModeType?, sort: Sort?): List<T> {
        return queryManager
            .getQuery(spec, sort ?: Sort.unsorted())
            .resultList
            .also {
                if (lockMode != null) {
                    it.forEach { entity ->
                        entityManager.lock(entity, lockMode)
                    }
                }
            }
    }

    override fun count(spec: Specification<T>): Long {
        return queryManager.executeCountQuery(
            queryManager.getCountQuery(spec, entityInformation.javaType)
        )
    }

    override fun delete(spec: Specification<T>) {
        crudRepository.delete(find(spec) ?: throw EmptyResultDataAccessException("No ${entityInformation.javaType} entity exists!", 1))
    }

    override fun deleteAll(spec: Specification<T>) {
        crudRepository.deleteAll(findAll(spec, lockMode = LockModeType.PESSIMISTIC_WRITE))
    }
}
