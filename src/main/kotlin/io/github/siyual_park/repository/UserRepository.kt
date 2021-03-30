package io.github.siyual_park.repository

import io.github.siyual_park.model.user.User
import io.github.siyual_park.repository.base.CustomRepository
import io.github.siyual_park.repository.base.SimpleCustomRepository
import io.github.siyual_park.repository.specification.UserSpecification
import org.springframework.stereotype.Component
import javax.persistence.EntityManager
import javax.persistence.LockModeType
import javax.transaction.Transactional

@Component
class UserRepository(
    entityManager: EntityManager
) : CustomRepository<User, String, UserSpecification> by SimpleCustomRepository.of(entityManager, UserSpecification) {
    @Transactional
    fun findByNameOrFail(name: String, lockMode: LockModeType? = null): User = findOrFail({ withName(name) }, lockMode)

    @Transactional
    fun findByName(name: String, lockMode: LockModeType? = null): User? = find({ withName(name) }, lockMode)
}
