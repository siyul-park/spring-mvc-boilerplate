package io.github.siyual_park.repository.query

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.query.Jpa21Utils
import org.springframework.data.jpa.repository.query.JpaEntityGraph
import org.springframework.data.jpa.repository.support.CrudMethodMetadata
import org.springframework.data.jpa.repository.support.JpaEntityInformation
import org.springframework.data.jpa.repository.support.MutableQueryHints
import org.springframework.data.jpa.repository.support.QueryHints
import org.springframework.data.util.Optionals
import java.util.Optional
import java.util.function.BiConsumer
import javax.persistence.EntityManager

class DefaultQueryHints(
    private val information: JpaEntityInformation<*, *>,
    private val metadata: CrudMethodMetadata,
    private val entityManager: EntityManager?,
    private val forCounts: Boolean
) : QueryHints {
    override fun withFetchGraphs(em: EntityManager): QueryHints {
        return DefaultQueryHints(information, metadata, em, forCounts)
    }

    override fun forCounts(): QueryHints {
        return DefaultQueryHints(information, metadata, entityManager, true)
    }

    override fun forEach(action: BiConsumer<String, Any>) {
        combineHints().forEach(action)
    }

    private fun combineHints(): QueryHints {
        return QueryHints.from(
            if (forCounts) metadata.queryHintsForCount else metadata.queryHints,
            fetchGraphs
        )
    }

    private val fetchGraphs: QueryHints
        get() = Optionals
            .mapIfAllPresent(
                Optional.ofNullable(entityManager),
                metadata.entityGraph
            ) { em, graph ->
                Jpa21Utils.getFetchGraphHint(em, getEntityGraph(graph), information.javaType)
            }
            .orElse(MutableQueryHints())

    private fun getEntityGraph(entityGraph: EntityGraph): JpaEntityGraph {
        val fallbackName = information.entityName + "." + metadata.method.name
        return JpaEntityGraph(entityGraph, fallbackName)
    }

    companion object {
        fun of(information: JpaEntityInformation<*, *>, metadata: CrudMethodMetadata): QueryHints {
            return DefaultQueryHints(information, metadata, null, false)
        }
    }
}
