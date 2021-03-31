package io.github.siyual_park.domain.security

import io.github.siyual_park.model.scope.ScopeToken
import io.github.siyual_park.model.user.User
import io.github.siyual_park.repository.ScopeTokenRelationRepository
import io.github.siyual_park.repository.ScopeTokenRepository
import io.github.siyual_park.repository.UserScopeTokenRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component

@Component
class ScopeFetchProcessor(
    private val scopeTokenRepository: ScopeTokenRepository,
    private val scopeTokenRelationRepository: ScopeTokenRelationRepository,
    private val userScopeTokenRepository: UserScopeTokenRepository
) {
    @Cacheable("ScopeFetchProcessor.process(User)")
    fun process(user: User): Set<ScopeToken> {
        return userScopeTokenRepository.findAllByUser(user)
            .map { it.scopeToken }
            .map { process(it) }
            .fold(mutableSetOf()) { acc, set ->
                acc.apply {
                    addAll(set)
                }
            }
    }

    @Cacheable("ScopeFetchProcessor.process(ScopeToken)")
    fun process(scopeToken: ScopeToken): Set<ScopeToken> {
        val children = scopeTokenRelationRepository.findAllByParent(scopeToken)
            .map { it.child }
            .toSet()
        if (children.isEmpty()) {
            return setOf(scopeToken)
        }
        return children
    }

    @Cacheable("ScopeFetchProcessor.process(String)")
    fun process(scope: String): Set<ScopeToken> {
        val names = scope.split(" ")
        return scopeTokenRepository.findAllByNameIn(names).toSet()
    }
}
