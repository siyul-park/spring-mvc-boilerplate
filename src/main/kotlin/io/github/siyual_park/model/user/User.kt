package io.github.siyual_park.model.user

import io.github.siyual_park.model.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Index
import javax.persistence.Table

@Entity
@Table(name = "users", indexes = [Index(name = "index_users_name", columnList = "name")])
data class User(
    @Column(unique = true, nullable = false)
    var name: String,
    @Column(nullable = false)
    var nickname: String,
    @Column(nullable = false)
    var password: String
) : BaseEntity()
