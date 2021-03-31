package io.github.siyual_park.repository.specification

import io.github.siyual_park.model.scope.ScopeToken
import io.github.siyual_park.repository.expansion.get
import org.springframework.data.jpa.domain.Specification

object ScopeTokenSpecification {
    fun withName(name: String) = Specification<ScopeToken> { root, _, builder ->
        builder.equal(root[ScopeToken::name], name)
    }
}
