package io.github.siyual_park.repository.base

import org.springframework.data.repository.NoRepositoryBean
import java.time.Duration
import java.time.temporal.ChronoUnit
import javax.persistence.EntityNotFoundException

@NoRepositoryBean
open class SimpleCachedByIdRepository<T : Any, K : Any>(
    repository: CrudRepository<T, K>,
    size: Long = 50000,
    duration: Duration = Duration.of(15, ChronoUnit.MINUTES)
) : CachedRepository<T, K> by SimpleCachedRepository.of(repository, size, duration) {
    fun findByIdOrFail(id: K): T = findById(id) ?: throw EntityNotFoundException("$id is not founded")

    fun findById(id: K): T? = get(id)
}
