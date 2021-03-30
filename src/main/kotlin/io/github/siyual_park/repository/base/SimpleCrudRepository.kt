package io.github.siyual_park.repository.base

import io.github.siyual_park.repository.expansion.EntityInformationProvider
import io.github.siyual_park.repository.patch.Patch
import io.github.siyual_park.repository.query.QueryManagerProvider
import io.github.siyual_park.repository.specification.ByIdsSpecification
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.provider.PersistenceProvider
import org.springframework.data.jpa.repository.query.QueryUtils
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.util.ProxyUtils
import javax.persistence.EntityExistsException
import javax.persistence.EntityManager
import javax.persistence.EntityNotFoundException
import javax.persistence.LockModeType
import javax.persistence.TypedQuery
import kotlin.reflect.KClass

@NoRepositoryBean
class SimpleCrudRepository<T : Any, ID>(
    override val clazz: KClass<T>,
    override val entityManager: EntityManager
) : CrudRepository<T, ID> {
    private val provider = PersistenceProvider.fromEntityManager(entityManager)
    private val entityInformation = EntityInformationProvider.of(clazz, entityManager)

    private val queryManager = QueryManagerProvider.of(clazz, entityManager)

    override fun <S : T> create(entity: S): S = if (entityInformation.isNew(entity)) {
        entityManager.persist(entity)
        entity
    } else {
        throw EntityExistsException("${entityInformation.getId(entity)} is exists in ${entityInformation.entityName}")
    }

    override fun <S : T> createAll(entities: Iterable<S>): List<S> = mutableListOf<S>().apply {
        for (entity in entities) {
            add(create(entity))
        }
    }

    override fun updateById(id: ID, patch: Patch<T>): T {
        return findById(id, LockModeType.PESSIMISTIC_WRITE)
            ?.let { update(it, patch) }
            ?: throw EntityNotFoundException("No ${entityInformation.javaType} entity exists!")
    }

    override fun update(entity: T, patch: Patch<T>): T = if (!entityInformation.isNew(entity)) {
        entityManager.merge(patch.apply(entity))
    } else {
        throw EmptyResultDataAccessException(
            "No ${entityInformation.javaType} entity with id ${entityInformation.getId(entity)} exists!",
            1
        )
    }

    override fun <S : T> upsert(entity: S): S = if (entityInformation.isNew(entity)) {
        entityManager.persist(entity)
        entity
    } else {
        entityManager.merge(entity)
    }

    override fun <S : T> upsertAll(entities: Iterable<S>): List<S> = mutableListOf<S>().apply {
        for (entity in entities) {
            add(upsert(entity))
        }
    }

    override fun findByIdOrFail(id: ID, lockMode: LockModeType?): T = findById(id, lockMode) ?: throw EntityNotFoundException("$id is not founded in ${entityInformation.entityName}")

    override fun findById(id: ID, lockMode: LockModeType?): T? = entityManager.find(clazz.java, id, lockMode)

    override fun existsById(id: ID): Boolean {
        if (entityInformation.idAttribute == null) {
            return findById(id) != null
        }

        val placeholder: String = provider.countQueryPlaceholder
        val entityName = entityInformation.entityName
        val idAttributeNames = entityInformation.idAttributeNames
        val existsQuery = QueryUtils.getExistsQueryString(entityName, placeholder, idAttributeNames)

        val query = entityManager.createQuery(existsQuery, Long::class.javaObjectType)

        if (!entityInformation.hasCompositeId()) {
            query.setParameter(idAttributeNames.iterator().next(), id)
            return query.singleResult == 1L
        }

        for (idAttributeName in idAttributeNames) {
            val idAttributeValue = entityInformation.getCompositeIdAttributeValue(id!!, idAttributeName!!)
            val complexIdParameterValueDiscovered = (
                idAttributeValue != null &&
                    !query.getParameter(idAttributeName).parameterType.isAssignableFrom(idAttributeValue.javaClass)
                )
            if (complexIdParameterValueDiscovered) {

                return findById(id) != null
            }
            query.setParameter(idAttributeName, idAttributeValue)
        }

        return query.singleResult == 1L
    }

    override fun findAll(sort: Sort?, lockMode: LockModeType?): List<T> {
        return queryManager
            .getQuery(null, sort ?: Sort.unsorted())
            .resultList
            .also {
                if (lockMode != null) {
                    it.forEach { entity ->
                        entityManager.lock(entity, lockMode)
                    }
                }
            }
    }

    override fun findAllById(ids: Iterable<ID>, sort: Sort?): List<T> {
        if (!ids.iterator().hasNext()) {
            return emptyList()
        }

        if (entityInformation.hasCompositeId()) {
            val results = mutableListOf<T>()
            for (id in ids) {
                findById(id)?.let { results.add(it) }
            }
            return results
        }

        val idCollection = ids.toList()

        val specification = ByIdsSpecification(entityInformation)
        val query: TypedQuery<T> = queryManager.getQuery(specification, sort ?: Sort.unsorted())

        return query.setParameter(specification.parameter, idCollection).resultList
    }

    override fun count(): Long {
        return entityManager
            .createQuery(queryManager.getCountQueryString(), Long::class.javaObjectType)
            .singleResult
    }

    override fun deleteById(id: ID) {
        delete(findById(id) ?: throw EmptyResultDataAccessException("No ${entityInformation.javaType} entity with id $id exists!", 1))
    }

    override fun delete(entity: T) {
        if (entityInformation.isNew(entity)) {
            return
        }

        val type = ProxyUtils.getUserClass(entity)
        entityManager.find(type, entityInformation.getId(entity)) ?: return

        entityManager.remove(
            if (entityManager.contains(entity)) entity
            else entityManager.merge(entity)
        )
    }

    override fun deleteAll(entities: Iterable<T>) {
        if (!entities.iterator().hasNext()) {
            return
        }

        QueryUtils.applyAndBind(
            queryManager.getDeleteAllQueryString(),
            entities,
            entityManager
        ).executeUpdate()
    }

    override fun deleteAll() {
        entityManager.createQuery(queryManager.getDeleteAllQueryString()).executeUpdate()
    }
}
