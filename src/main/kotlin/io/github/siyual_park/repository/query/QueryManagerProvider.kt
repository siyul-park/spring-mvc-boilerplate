package io.github.siyual_park.repository.query

import org.springframework.util.ConcurrentReferenceHashMap
import javax.persistence.EntityManager
import kotlin.reflect.KClass

object QueryManagerProvider {
    private val cache = ConcurrentReferenceHashMap<Pair<KClass<*>, EntityManager>, QueryManager<*>>()

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> of(clazz: KClass<T>, entityManager: EntityManager): QueryManager<T> {
        val cached = cache[clazz to entityManager]
        if (cached != null) {
            return cached as QueryManager<T>
        }
        return QueryManager(clazz, entityManager)
            .apply { cache[clazz to entityManager] = this }
    }
}
