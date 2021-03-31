package io.github.siyual_park.repository

import io.github.siyual_park.model.scope.UserScopeToken
import io.github.siyual_park.model.user.User
import io.github.siyual_park.repository.base.CustomRepository
import io.github.siyual_park.repository.base.SimpleCustomRepository
import io.github.siyual_park.repository.specification.UserScopeTokenSpecification
import org.springframework.stereotype.Component
import javax.persistence.EntityManager
import javax.persistence.LockModeType
import javax.transaction.Transactional

@Component
class UserScopeTokenRepository(
    entityManager: EntityManager
) : CustomRepository<UserScopeToken, String, UserScopeTokenSpecification> by SimpleCustomRepository.of(entityManager, UserScopeTokenSpecification) {
    @Transactional
    fun findAllByUser(user: User, lockMode: LockModeType? = null): List<UserScopeToken> = findAll({ withUser(user) }, lockMode = lockMode)

    @Transactional
    fun findAllByUser(userId: String, lockMode: LockModeType? = null): List<UserScopeToken> = findAll({ withUser(userId) }, lockMode = lockMode)

    @Transactional
    fun deleteAllByUser(user: User): Unit = deleteAll { withUser(user) }

    @Transactional
    fun deleteAllByUser(userId: String): Unit = deleteAll { withUser(userId) }
}
