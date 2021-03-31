package io.github.siyual_park.domain.scope

import io.github.siyual_park.model.scope.ScopeToken
import io.github.siyual_park.model.user.User
import io.github.siyual_park.repository.ScopeTokenRelationRepository
import io.github.siyual_park.repository.ScopeTokenRepository
import io.github.siyual_park.repository.UserScopeTokenRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component
import javax.transaction.Transactional

@Component
class ScopeFetchExecutor(
    private val scopeTokenRepository: ScopeTokenRepository,
    private val scopeTokenRelationRepository: ScopeTokenRelationRepository,
    private val userScopeTokenRepository: UserScopeTokenRepository
) {
    @Transactional
    @Cacheable("ScopeFetchExecutor.execute(User)")
    fun execute(user: User, depth: Int? = null): Set<ScopeToken> {
        return userScopeTokenRepository.findAllByUser(user)
            .map { it.scopeToken }
            .map { execute(it, depth) }
            .fold(mutableSetOf()) { acc, set ->
                acc.apply { addAll(set) }
            }
    }

    @Transactional
    @Cacheable("ScopeFetchExecutor.execute(String)")
    fun execute(scope: String, depth: Int? = null): Set<ScopeToken> {
        val names = scope.split(" ")
        return scopeTokenRepository.findAllByNameIn(names)
            .map { execute(it, depth) }
            .fold(mutableSetOf()) { acc, set ->
                acc.apply { addAll(set) }
            }
    }

    @Transactional
    @Cacheable("ScopeFetchExecutor.execute(ScopeToken)")
    fun execute(scopeToken: ScopeToken, depth: Int? = null): Set<ScopeToken> {
        if (depth == 0) {
            return setOf(scopeToken)
        }

        val children = scopeTokenRelationRepository.findAllByParent(scopeToken)
            .map { it.child }
            .toSet()
        if (children.isEmpty()) {
            return setOf(scopeToken)
        }

        return children
            .map { execute(it, depth?.let { depth - 1 }) }
            .fold(mutableSetOf(scopeToken)) { acc, set ->
                acc.apply { addAll(set) }
            }
    }
}
