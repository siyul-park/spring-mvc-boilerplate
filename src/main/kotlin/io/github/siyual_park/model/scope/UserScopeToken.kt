package io.github.siyual_park.model.scope

import io.github.siyual_park.model.BaseEntity
import io.github.siyual_park.model.user.User
import javax.persistence.Entity
import javax.persistence.Index
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Entity
@Table(
    name = "user_scope_tokens",
    indexes = [
        Index(name = "index_user_scope_tokens_user_id", columnList = "user_id"),
        Index(name = "index_user_scope_tokens_scope_token_id", columnList = "scope_token_id")
    ],
    uniqueConstraints = [
        UniqueConstraint(
            columnNames = ["user_id", "scope_token_id"]
        )
    ]
)
data class UserScopeToken(
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,
    @ManyToOne
    @JoinColumn(name = "scope_token_id", nullable = false)
    var scopeToken: ScopeToken
) : BaseEntity()
