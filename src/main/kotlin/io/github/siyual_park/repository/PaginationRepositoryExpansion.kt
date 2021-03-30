package io.github.siyual_park.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.transaction.annotation.Transactional

@NoRepositoryBean
interface PaginationRepositoryExpansion<T : Any> {
    @Transactional
    fun findAll(pageable: Pageable): Page<T>
}
