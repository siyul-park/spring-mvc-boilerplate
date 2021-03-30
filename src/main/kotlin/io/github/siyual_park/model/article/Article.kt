package io.github.siyual_park.model.article

import io.github.siyual_park.model.BaseEntity
import io.github.siyual_park.model.category.Category
import org.hibernate.annotations.DynamicUpdate
import org.springframework.util.MimeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Index
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "articles", indexes = [Index(name = "index_articles_category_id", columnList = "category_id")])
@DynamicUpdate
data class Article(
    @Column(nullable = false)
    var title: String,
    @Column(nullable = false)
    var content: String,
    @Column(nullable = false)
    var contentType: MimeType,
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    var category: Category
) : BaseEntity() {
    var views: Long = 0
}
