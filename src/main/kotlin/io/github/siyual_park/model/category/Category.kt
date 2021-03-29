package io.github.siyual_park.model.category

import io.github.siyual_park.model.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "categories")
data class Category(
    @Column(unique = true)
    var name: String,
) : BaseEntity()
