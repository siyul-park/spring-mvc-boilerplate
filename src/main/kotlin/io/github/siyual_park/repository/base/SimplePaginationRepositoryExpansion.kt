package io.github.siyual_park.repository.base

import io.github.siyual_park.repository.expansion.EntityInformationProvider
import io.github.siyual_park.repository.query.QueryManagerProvider
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.support.PageableExecutionUtils
import javax.persistence.EntityManager
import javax.persistence.TypedQuery
import kotlin.reflect.KClass

@NoRepositoryBean
open class SimplePaginationRepositoryExpansion<T : Any, ID> (
    val crudRepository: CrudRepository<T, ID>,
    val clazz: KClass<T>,
    val entityManager: EntityManager
) : PaginationRepositoryExpansion<T> {

    private val entityInformation = EntityInformationProvider.of(clazz, entityManager)
    private val queryManager = QueryManagerProvider.of(clazz, entityManager)

    override fun findAll(pageable: Pageable): Page<T> {
        return if (pageable.isUnpaged) {
            PageImpl(crudRepository.findAll())
        } else {
            val query: TypedQuery<T> = queryManager.getQuery(null, pageable)
            return when (pageable.isUnpaged) {
                true -> PageImpl(query.resultList)
                false -> readPage(
                    query,
                    entityInformation.javaType,
                    pageable
                )
            }
        }
    }

    private fun <S : T> readPage(
        query: TypedQuery<S>,
        domainClass: Class<S>,
        pageable: Pageable
    ): Page<S> {
        if (pageable.isPaged) {
            query.firstResult = pageable.offset.toInt()
            query.maxResults = pageable.pageSize
        }

        return PageableExecutionUtils.getPage(
            query.resultList,
            pageable
        ) {
            queryManager.executeCountQuery(queryManager.getCountQuery(null, domainClass))
        }
    }
}
