package io.github.siyual_park.repository

import io.github.siyual_park.model.scope.ScopeToken
import io.github.siyual_park.model.scope.ScopeTokenRelation
import io.github.siyual_park.repository.base.CustomRepository
import io.github.siyual_park.repository.base.SimpleCustomRepository
import io.github.siyual_park.repository.expansion.and
import io.github.siyual_park.repository.specification.ScopeTokenRelationSpecification
import org.springframework.stereotype.Component
import javax.persistence.EntityManager
import javax.persistence.LockModeType
import javax.transaction.Transactional

@Component
class ScopeTokenRelationRepository(
    entityManager: EntityManager
) : CustomRepository<ScopeTokenRelation, String, ScopeTokenRelationSpecification> by SimpleCustomRepository.of(entityManager, ScopeTokenRelationSpecification) {
    @Transactional
    fun deleteAllByChild(child: ScopeToken) = deleteAll { withChild(child) }

    @Transactional
    fun deleteAllByChild(childId: String) = deleteAll { withChild(childId) }

    @Transactional
    fun deleteAllByParent(parent: ScopeToken) = deleteAll { withParent(parent) }

    @Transactional
    fun deleteAllByParent(parentId: String) = deleteAll { withParent(parentId) }

    @Transactional
    fun findByParentAndChild(parent: ScopeToken, child: ScopeToken, lockMode: LockModeType? = null) =
        find({ withParent(parent) and withChild(child) }, lockMode = lockMode)

    @Transactional
    fun findAllByChild(child: ScopeToken, lockMode: LockModeType? = null) = findAll({ withChild(child) }, lockMode = lockMode)

    @Transactional
    fun findAllByChild(childId: String, lockMode: LockModeType? = null) = findAll({ withChild(childId) }, lockMode = lockMode)

    @Transactional
    fun findAllByParent(parent: ScopeToken, lockMode: LockModeType? = null) = findAll({ withParent(parent) }, lockMode = lockMode)

    @Transactional
    fun findAllByParent(parentId: String, lockMode: LockModeType? = null) = findAll({ withParent(parentId) }, lockMode = lockMode)
}
