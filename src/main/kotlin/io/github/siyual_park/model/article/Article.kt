package io.github.siyual_park.model.article

import io.github.siyual_park.model.BaseEntity
import io.github.siyual_park.model.category.Category
import org.springframework.util.MimeType
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "articles")
data class Article(
    var title: String,
    var content: String,
    var contentType: MimeType,
    @ManyToOne
    @JoinColumn(name = "category_id")
    var category: Category
) : BaseEntity() {
    var views: Long = 0
}
