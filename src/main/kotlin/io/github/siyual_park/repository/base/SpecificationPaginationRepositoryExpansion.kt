package io.github.siyual_park.repository.base

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.transaction.annotation.Transactional

@NoRepositoryBean
interface SpecificationPaginationRepositoryExpansion<T : Any> {
    @Transactional
    fun findAll(spec: Specification<T>, pageable: Pageable): Page<T>
}
