package io.github.siyual_park.repository.query

import io.github.siyual_park.repository.expansion.EntityInformationProvider
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.provider.PersistenceProvider
import org.springframework.data.jpa.repository.query.QueryUtils
import org.springframework.data.jpa.repository.support.CrudMethodMetadata
import org.springframework.data.jpa.repository.support.QueryHints
import org.springframework.data.jpa.repository.support.QueryHints.NoHints
import javax.persistence.EntityManager
import javax.persistence.Query
import javax.persistence.TypedQuery
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root
import kotlin.reflect.KClass

open class QueryManager<T : Any>(
    clazz: KClass<T>,
    private val entityManager: EntityManager,
) {
    private val provider = PersistenceProvider.fromEntityManager(entityManager)
    private val entityInformation = EntityInformationProvider.of(clazz, entityManager)

    private val metadata: CrudMethodMetadata? = null

    fun executeCountQuery(query: TypedQuery<Long>): Long {
        val totals = query.resultList
        var total = 0L
        for (element in totals) {
            total += element ?: 0
        }
        return total
    }

    fun getDeleteAllQueryString(): String {
        return QueryUtils.getQueryString(QueryUtils.DELETE_ALL_QUERY_STRING, entityInformation.entityName)
    }

    fun getCountQueryString(): String {
        val countQuery = String.format(QueryUtils.COUNT_QUERY_STRING, provider.getCountQueryPlaceholder(), "%s")
        return QueryUtils.getQueryString(countQuery, entityInformation.entityName)
    }

    fun getQuery(spec: Specification<T>?, pageable: Pageable): TypedQuery<T> {
        val sort = if (pageable.isPaged) pageable.sort else Sort.unsorted()
        return getQuery(spec, getDomainClass(), sort)
    }

    fun <S : T> getQuery(
        spec: Specification<S>?,
        domainClass: Class<S>,
        pageable: Pageable
    ): TypedQuery<S> {
        val sort = if (pageable.isPaged) pageable.sort else Sort.unsorted()
        return getQuery(spec, domainClass, sort)
    }

    fun getQuery(spec: Specification<T>?, sort: Sort): TypedQuery<T> {
        return getQuery(spec, getDomainClass(), sort)
    }

    fun <S : T> getQuery(spec: Specification<S>?, domainClass: Class<S>, sort: Sort): TypedQuery<S> {
        val builder: CriteriaBuilder = entityManager.criteriaBuilder
        val query = builder.createQuery(domainClass)
        val root: Root<S> = applySpecificationToCriteria(spec, domainClass, query)
        query.select(root)
        if (sort.isSorted) {
            query.orderBy(QueryUtils.toOrders(sort, root, builder))
        }
        return applyRepositoryMethodMetadata(entityManager.createQuery(query))
    }

    fun <S : T> getCountQuery(spec: Specification<S>?, domainClass: Class<S>): TypedQuery<Long> {
        val builder: CriteriaBuilder = entityManager.criteriaBuilder
        val query = builder.createQuery(Long::class.java)
        val root: Root<S> = applySpecificationToCriteria(spec, domainClass, query)
        if (query.isDistinct) {
            query.select(builder.countDistinct(root))
        } else {
            query.select(builder.count(root))
        }

        // Remove all Orders the Specifications might have applied
        query.orderBy(emptyList())
        return entityManager.createQuery(query)
    }

    private fun getDomainClass(): Class<T> {
        return entityInformation.javaType
    }

    private fun <S, U : T> applySpecificationToCriteria(
        spec: Specification<U>?,
        domainClass: Class<U>,
        query: CriteriaQuery<S>
    ): Root<U> {
        val root = query.from(domainClass)
        if (spec == null) {
            return root
        }
        val builder: CriteriaBuilder = entityManager.criteriaBuilder
        val predicate = spec.toPredicate(root, query, builder)
        if (predicate != null) {
            query.where(predicate)
        }
        return root
    }

    private fun <S> applyRepositoryMethodMetadata(query: TypedQuery<S>): TypedQuery<S> {
        if (metadata == null) {
            return query
        }
        val type = metadata.lockModeType
        val toReturn = if (type == null) query else query.setLockMode(type)
        applyQueryHints(toReturn)
        return toReturn
    }

    private fun applyQueryHints(query: Query) {
        getQueryHints().withFetchGraphs(entityManager).forEach { hintName: String?, value: Any? ->
            query.setHint(
                hintName,
                value
            )
        }
    }

    private fun getQueryHints(): QueryHints {
        return if (metadata == null) NoHints.INSTANCE else DefaultQueryHints.of(entityInformation, metadata)
    }
}
