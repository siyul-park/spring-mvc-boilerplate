package io.github.siyual_park.repository

import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface SpecificationRepository<T : Any, ID> : CrudRepository<T, ID>, SpecificationRepositoryExpansion<T>
