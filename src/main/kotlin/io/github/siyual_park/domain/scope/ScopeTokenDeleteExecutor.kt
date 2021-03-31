package io.github.siyual_park.domain.scope

import io.github.siyual_park.model.scope.ScopeToken
import io.github.siyual_park.repository.ScopeTokenRelationRepository
import io.github.siyual_park.repository.ScopeTokenRepository
import org.springframework.stereotype.Component
import javax.persistence.LockModeType
import javax.transaction.Transactional

@Component
class ScopeTokenDeleteExecutor(
    private val scopeTokenRepository: ScopeTokenRepository,
    private val scopeTokenRelationRepository: ScopeTokenRelationRepository
) {
    @Transactional
    fun executeByName(scopeTokenName: String) {
        val scopeToken = scopeTokenRepository.findByNameOrFail(scopeTokenName, lockMode = LockModeType.PESSIMISTIC_WRITE)
        execute(scopeToken)
    }

    @Transactional
    fun executeById(scopeTokenId: String) {
        val scopeToken = scopeTokenRepository.findByIdOrFail(scopeTokenId, lockMode = LockModeType.PESSIMISTIC_WRITE)
        execute(scopeToken)
    }

    @Transactional
    fun execute(scopeToken: ScopeToken) {
        scopeTokenRelationRepository.deleteAllByChild(scopeToken)
        scopeTokenRelationRepository.deleteAllByParent(scopeToken)
        scopeTokenRepository.delete(scopeToken)
    }
}
