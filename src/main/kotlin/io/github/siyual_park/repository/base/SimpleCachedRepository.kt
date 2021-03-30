package io.github.siyual_park.repository.base

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import org.springframework.data.repository.NoRepositoryBean
import java.time.Duration
import java.time.temporal.ChronoUnit
import java.util.concurrent.ExecutionException

@NoRepositoryBean
class SimpleCachedRepository<T : Any, K : Any>(
    private val cache: LoadingCache<K, T>
) : CachedRepository<T, K> {
    override fun get(key: K): T? {
        return try {
            cache.get(key)
        } catch (e: ExecutionException) {
            null
        }
    }

    override fun set(key: K, value: T?): T? {
        if (value == null) {
            cache.invalidate(key)
        } else {
            cache.put(key, value)
        }
        return value
    }

    override fun clear() {
        cache.cleanUp()
    }

    companion object {
        fun <T : Any, ID> of(
            repository: CrudRepository<T, ID>,
            size: Long = 50000,
            duration: Duration = Duration.of(15, ChronoUnit.MINUTES)
        ) = SimpleCachedRepository(
            CacheBuilder.newBuilder()
                .maximumSize(size)
                .expireAfterWrite(duration)
                .build(
                    object : CacheLoader<ID, T>() {
                        override fun load(id: ID): T {
                            return repository.findByIdOrFail(id)
                        }
                    }
                )
        )
    }
}
