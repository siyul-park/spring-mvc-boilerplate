package io.github.siyual_park.model.article

import io.github.siyual_park.model.BaseEntity
import org.springframework.util.MimeType
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "articles")
data class Article(
    var title: String,
    var content: String,
    var contentType: MimeType
) : BaseEntity() {
    var views: Long = 0
}
