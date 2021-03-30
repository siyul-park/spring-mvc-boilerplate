package io.github.siyual_park.repository.base

import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface PaginationRepository<T : Any, ID> : CrudRepository<T, ID>, PaginationRepositoryExpansion<T>
