package io.github.siyual_park.model.user

import io.github.siyual_park.model.BaseEntity
import javax.persistence.Column
import javax.persistence.ElementCollection
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
    var password: String,
    @ElementCollection
    @Column(nullable = false)
    var scope: Set<String> = setOf(),
    @Column(nullable = false)
    var hashAlgorithm: String = defaultHashAlgorithm
) : BaseEntity() {
    companion object {
        const val defaultHashAlgorithm = "SHA-256"
    }
}
