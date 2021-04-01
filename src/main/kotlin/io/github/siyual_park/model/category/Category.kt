package io.github.siyual_park.model.category

import io.github.siyual_park.model.BaseEntity
import io.github.siyual_park.model.user.User
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Index
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(
    name = "categories",
    indexes = [
        Index(name = "index_categories_name", columnList = "name"),
        Index(name = "index_categories_owner_id", columnList = "owner_id")
    ]
)
data class Category(
    @Column(unique = true, nullable = false)
    var name: String,
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    val owner: User
) : BaseEntity()
