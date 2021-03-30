package io.github.siyual_park.repository.base

import org.springframework.data.repository.NoRepositoryBean
import javax.transaction.Transactional

@NoRepositoryBean
interface CachedRepository<T : Any, K> {
    @Transactional
    operator fun get(key: K): T?

    @Transactional
    operator fun set(key: K, value: T?): T?

    fun clear()
}
