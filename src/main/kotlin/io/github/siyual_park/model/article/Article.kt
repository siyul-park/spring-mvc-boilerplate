package io.github.siyual_park.model.article

import io.github.siyual_park.model.BaseEntity
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "articles")
data class Article(
    var title: String,
    var content: String,
) : BaseEntity() {
    var views: Long = 0
}
