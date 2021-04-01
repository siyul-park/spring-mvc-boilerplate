package io.github.siyual_park.domain.scope

import io.github.siyual_park.model.scope.ScopeToken
import io.github.siyual_park.model.scope.ScopeTokenRelation
import io.github.siyual_park.repository.ScopeTokenRelationRepository
import io.github.siyual_park.repository.ScopeTokenRepository
import org.springframework.stereotype.Component
import javax.transaction.Transactional

@Component
class ScopePrepareManager(
    private val scopeTokenRepository: ScopeTokenRepository,
    private val scopeTokenRelationRepository: ScopeTokenRelationRepository
) {
    @Transactional
    fun required(scope: Collection<ScopeToken>): List<ScopeToken> {
        return scope.map { required(it) }
    }

    @Transactional
    fun required(scopeToken: ScopeToken): ScopeToken {
        return scopeTokenRepository.findByName(scopeToken.name)
            ?: scopeTokenRepository.create(scopeToken)
    }

    @Transactional
    fun relation(parent: ScopeToken, child: ScopeToken): ScopeTokenRelation {
        return scopeTokenRelationRepository.findByParentAndChild(parent, child)
            ?: scopeTokenRelationRepository.create(ScopeTokenRelation(parent, child))
    }
}
