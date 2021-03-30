package io.github.siyual_park.model.scope

import io.github.siyual_park.model.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Index
import javax.persistence.Table

@Entity
@Table(name = "scope_tokens", indexes = [Index(name = "index_scope_tokens_name", columnList = "name")])
data class ScopeToken(
    @Column(unique = true, nullable = false)
    var name: String,
    var description: String? = null,
) : BaseEntity()
