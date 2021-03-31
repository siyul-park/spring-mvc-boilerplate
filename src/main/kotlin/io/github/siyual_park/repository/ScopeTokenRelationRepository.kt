package io.github.siyual_park.repository

import io.github.siyual_park.model.scope.ScopeToken
import io.github.siyual_park.model.scope.ScopeTokenRelation
import io.github.siyual_park.repository.base.CustomRepository
import io.github.siyual_park.repository.base.SimpleCustomRepository
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
    fun findAllByParent(parent: ScopeToken, lockMode: LockModeType? = null): List<ScopeTokenRelation> = findAll({ withParent(parent) }, lockMode = lockMode)

    @Transactional
    fun findAllByParent(parentId: String, lockMode: LockModeType? = null): List<ScopeTokenRelation> = findAll({ withParent(parentId) }, lockMode = lockMode)
}
