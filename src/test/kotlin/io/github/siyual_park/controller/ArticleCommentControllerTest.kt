package io.github.siyual_park.controller

import io.github.siyual_park.factory.ArticleCreatePayloadMockFactory
import io.github.siyual_park.factory.CommentCreatePayloadMockFactory
import io.github.siyual_park.factory.RandomFactory
import io.github.siyual_park.model.article.Article
import io.github.siyual_park.model.article.ArticleCreatePayloadMapper
import io.github.siyual_park.model.category.Category
import io.github.siyual_park.model.comment.CommentCreatePayloadMapper
import io.github.siyual_park.repository.ArticleRepository
import io.github.siyual_park.repository.CategoryRepository
import io.github.siyual_park.repository.CommentRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import kotlin.math.ceil

@ControllerTest
class ArticleCommentControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val commentRepository: CommentRepository,
    private val articleRepository: ArticleRepository,
    private val categoryRepository: CategoryRepository
) {

    private val commentCreatePayloadMapper = CommentCreatePayloadMapper(articleRepository)
    private val articleCreatePayloadMapper = ArticleCreatePayloadMapper(categoryRepository)

    private lateinit var category: Category
    private lateinit var article: Article
    private lateinit var articleCreatePayloadMockFactory: ArticleCreatePayloadMockFactory
    private lateinit var commentCreatePayloadMockFactory: CommentCreatePayloadMockFactory

    @BeforeEach
    fun prepare() {
        category = categoryRepository.create(Category(RandomFactory.createString(10)))
        articleCreatePayloadMockFactory = ArticleCreatePayloadMockFactory(category)

        article = articleCreatePayloadMockFactory.create()
            .let { articleCreatePayloadMapper.map(it) }
            .let { articleRepository.create(it) }
        commentCreatePayloadMockFactory = CommentCreatePayloadMockFactory(article)
    }

    @Test
    fun testAllById() {
        commentCreatePayloadMockFactory.create()
            .let { commentCreatePayloadMapper.map(it) }
            .let { commentRepository.create(it) }

        val count = commentRepository.count { withArticle(article) }

        mockMvc.get("/articles/${article.id}/comments")
            .andExpect {
                status { isOk() }
                header {
                    string("Total-Count", count.toString())
                    string("Total-Page", ceil(count / 20.0).toInt().toString())
                }
            }
            .andReturn()
    }
}
