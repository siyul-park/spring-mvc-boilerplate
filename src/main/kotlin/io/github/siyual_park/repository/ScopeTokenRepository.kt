package io.github.siyual_park.repository

import io.github.siyual_park.model.scope.ScopeToken
import io.github.siyual_park.repository.base.CustomRepository
import io.github.siyual_park.repository.base.SimpleCustomRepository
import io.github.siyual_park.repository.specification.ScopeTokenSpecification
import org.springframework.stereotype.Component
import javax.persistence.EntityManager
import javax.persistence.LockModeType
import javax.transaction.Transactional

@Component
class ScopeTokenRepository(
    entityManager: EntityManager
) : CustomRepository<ScopeToken, String, ScopeTokenSpecification> by SimpleCustomRepository.of(entityManager, ScopeTokenSpecification) {
    @Transactional
    fun findAllByNameIn(names: Iterable<String>, lockMode: LockModeType? = null): List<ScopeToken> = findAll({ withNameIn(names) }, lockMode)

    @Transactional
    fun findByNameOrFail(name: String, lockMode: LockModeType? = null): ScopeToken = findOrFail({ withName(name) }, lockMode)

    @Transactional
    fun findByName(name: String, lockMode: LockModeType? = null): ScopeToken? = find({ withName(name) }, lockMode)
}
