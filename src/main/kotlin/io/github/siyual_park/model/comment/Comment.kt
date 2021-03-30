package io.github.siyual_park.model.comment

import io.github.siyual_park.model.BaseEntity
import io.github.siyual_park.model.article.Article
import org.springframework.util.MimeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Index
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "comments", indexes = [Index(name = "index_comments_article_id", columnList = "article_id")])
data class Comment(
    @Column(nullable = false)
    var content: String,
    @Column(nullable = false)
    var contentType: MimeType,
    @ManyToOne
    @JoinColumn(name = "article_id")
    var article: Article
) : BaseEntity()
