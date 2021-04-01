package io.github.siyual_park.domain.user

import io.github.siyual_park.model.scope.UserScopeToken
import io.github.siyual_park.model.user.User
import io.github.siyual_park.property.ScopeProperty
import io.github.siyual_park.repository.ScopeTokenRepository
import io.github.siyual_park.repository.UserRepository
import io.github.siyual_park.repository.UserScopeTokenRepository
import org.springframework.stereotype.Component
import javax.transaction.Transactional

@Component
class UserCreateExecutor(
    private val userRepository: UserRepository,
    private val userScopeTokenRepository: UserScopeTokenRepository,
    private val scopeTokenRepository: ScopeTokenRepository,
    private val scopeProperty: ScopeProperty
) {
    @Transactional
    fun execute(user: User) = userRepository.create(user).also { created ->
        scopeTokenRepository.findByName(scopeProperty.default)?.let {
            userScopeTokenRepository.create(UserScopeToken(created, it))
        }
    }
}
