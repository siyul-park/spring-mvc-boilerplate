package io.github.siyual_park.domain.user

import io.github.siyual_park.model.user.User
import io.github.siyual_park.repository.UserRepository
import io.github.siyual_park.repository.UserScopeTokenRepository
import org.springframework.stereotype.Component
import javax.persistence.LockModeType
import javax.transaction.Transactional

@Component
class UserDeleteExecutor(
    private val userRepository: UserRepository,
    private val userScopeTokenRepository: UserScopeTokenRepository
) {
    @Transactional
    fun execute(id: String) {
        val user = userRepository.findByIdOrFail(id, LockModeType.PESSIMISTIC_WRITE)
        execute(user)
    }

    @Transactional
    fun execute(user: User) {
        userScopeTokenRepository.deleteAllByUser(user)
        userRepository.delete(user)
    }
}
