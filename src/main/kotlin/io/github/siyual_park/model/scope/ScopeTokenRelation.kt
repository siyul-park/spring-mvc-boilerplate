package io.github.siyual_park.model.scope

import io.github.siyual_park.model.BaseEntity
import javax.persistence.Entity
import javax.persistence.Index
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Entity
@Table(
    name = "scope_token_relations",
    indexes = [
        Index(name = "index_scope_tokens_parent_id", columnList = "parent_id"),
        Index(name = "index_scope_tokens_child_id", columnList = "child_id")
    ],
    uniqueConstraints = [
        UniqueConstraint(
            columnNames = ["parent_id", "child_id"]
        )
    ]
)
data class ScopeTokenRelation(
    @ManyToOne
    @JoinColumn(name = "parent_id", nullable = false)
    var parent: ScopeToken,
    @ManyToOne
    @JoinColumn(name = "child_id", nullable = false)
    var child: ScopeToken
) : BaseEntity()
