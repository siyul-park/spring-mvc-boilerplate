package io.github.siyual_park.repository.base

import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface SpecificationDSLRepository<T : Any, ID, FACTORY> :
    SpecificationRepository<T, ID>,
    SpecificationDSLRepositoryExpansion<T, FACTORY>
