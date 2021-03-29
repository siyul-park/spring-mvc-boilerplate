package io.github.siyual_park.model.comment

import com.fasterxml.jackson.annotation.JsonIgnore
import io.github.siyual_park.model.BaseEntity
import io.github.siyual_park.model.article.Article
import org.springframework.util.MimeType
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "comments")
data class Comment(
    var content: String,
    var contentType: MimeType,
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "article_id")
    var article: Article
) : BaseEntity()
