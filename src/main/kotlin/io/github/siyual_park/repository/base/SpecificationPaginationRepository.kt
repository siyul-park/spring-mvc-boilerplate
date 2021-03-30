package io.github.siyual_park.repository.base

import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface SpecificationPaginationRepository<T : Any, ID> :
    SpecificationRepository<T, ID>,
    PaginationRepository<T, ID>,
    SpecificationPaginationRepositoryExpansion<T>
