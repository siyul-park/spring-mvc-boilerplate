package io.github.siyual_park.repository.expansion

import org.springframework.data.jpa.repository.support.JpaEntityInformation
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport
import org.springframework.util.ConcurrentReferenceHashMap
import javax.persistence.EntityManager
import kotlin.reflect.KClass

object EntityInformationProvider {
    private val cache = ConcurrentReferenceHashMap<Pair<KClass<*>, EntityManager>, JpaEntityInformation<*, *>>()

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> of(clazz: KClass<T>, entityManager: EntityManager): JpaEntityInformation<T, *> {
        val cached = cache[clazz to entityManager]
        if (cached != null) {
            return cached as JpaEntityInformation<T, *>
        }
        return JpaEntityInformationSupport.getEntityInformation(clazz.java, entityManager)
            .apply { cache[clazz to entityManager] = this }
    }
}
