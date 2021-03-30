package io.github.siyual_park.repository.specification

import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.support.JpaEntityInformation
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.ParameterExpression
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

class ByIdsSpecification<T>(private val entityInformation: JpaEntityInformation<T, *>) : Specification<T> {
    var parameter: ParameterExpression<Collection<*>>? = null

    override fun toPredicate(root: Root<T>, query: CriteriaQuery<*>, criteriaBuilder: CriteriaBuilder): Predicate? {
        val path = root[entityInformation.idAttribute]
        parameter = criteriaBuilder.parameter(Collection::class.java)
        return path.`in`(parameter)
    }
}
