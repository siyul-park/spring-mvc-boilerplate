package io.github.siyual_park.repository.specification

import io.github.siyual_park.model.scope.ScopeToken
import io.github.siyual_park.model.scope.ScopeTokenRelation
import io.github.siyual_park.repository.expansion.get
import org.springframework.data.jpa.domain.Specification

object ScopeTokenRelationSpecification {
    fun withChild(child: ScopeToken) = withChild(child.id!!)

    fun withChild(childId: String) = Specification<ScopeTokenRelation> { root, _, builder ->
        builder.equal(root[ScopeTokenRelation::child][ScopeToken::id], childId)
    }

    fun withParent(parent: ScopeToken) = withParent(parent.id!!)

    fun withParent(parentId: String) = Specification<ScopeTokenRelation> { root, _, builder ->
        builder.equal(root[ScopeTokenRelation::parent][ScopeToken::id], parentId)
    }
}
