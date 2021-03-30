package io.github.siyual_park.repository.base

interface CustomRepository<T : Any, ID, FACTORY> :
    SpecificationDSLRepository<T, ID, FACTORY>,
    SpecificationPaginationRepository<T, ID>
