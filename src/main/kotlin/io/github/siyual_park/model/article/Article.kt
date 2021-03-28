package io.github.siyual_park.model.article

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "articles")
data class Article(
    var title: String,
    var content: String,
    @field:Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: String? = null
)
