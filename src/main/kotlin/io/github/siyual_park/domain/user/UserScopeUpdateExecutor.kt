package io.github.siyual_park.domain.user

import io.github.siyual_park.model.scope.Scope
import io.github.siyual_park.model.scope.UserScopeToken
import io.github.siyual_park.model.user.User
import io.github.siyual_park.repository.UserScopeTokenRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UserScopeUpdateExecutor(
    private val userScopeTokenRepository: UserScopeTokenRepository,
) {
    @Transactional
    fun execute(user: User, scope: Scope?) {
        if (scope == null) {
            userScopeTokenRepository.deleteAllByUser(user)
        } else {
            val existed = userScopeTokenRepository.findAllByUser(user)
            val existedScope = existed.map { it.scopeToken }

            val wantRemove = existed.filterNot { scope.contains(it.scopeToken) }
            val wantCreated = scope.filterNot { existedScope.contains(it) }
                .map { UserScopeToken(user, it) }

            userScopeTokenRepository.deleteAll(wantRemove)
            userScopeTokenRepository.createAll(wantCreated)
        }
    }
}
