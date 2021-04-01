package io.github.siyual_park.controller

import io.github.siyual_park.domain.article.ArticleCreatePayloadMapper
import io.github.siyual_park.domain.user.UserCreateExecutor
import io.github.siyual_park.domain.user.UserCreatePayloadMapper
import io.github.siyual_park.factory.ArticleCreatePayloadMockFactory
import io.github.siyual_park.factory.RandomFactory
import io.github.siyual_park.factory.UserCreatePayloadMockFactory
import io.github.siyual_park.model.category.Category
import io.github.siyual_park.model.user.User
import io.github.siyual_park.repository.ArticleRepository
import io.github.siyual_park.repository.CategoryRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import kotlin.math.ceil

@ControllerTest
class CategorizeArticleControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val articleRepository: ArticleRepository,
    private val categoryRepository: CategoryRepository,
    private val userCreateExecutor: UserCreateExecutor,
    private val articleCreatePayloadMapper: ArticleCreatePayloadMapper,
    private val userCreatePayloadMapper: UserCreatePayloadMapper,
    private val userCreatePayloadMockFactory: UserCreatePayloadMockFactory
) {

    private lateinit var user: User
    private lateinit var category: Category
    private lateinit var articleCreatePayloadMockFactory: ArticleCreatePayloadMockFactory

    @BeforeEach
    fun prepare() {
        user = userCreatePayloadMockFactory.create()
            .let { userCreatePayloadMapper.map(it) }
            .let { userCreateExecutor.execute(it) }

        category = categoryRepository.create(Category(RandomFactory.createString(10), user))
        articleCreatePayloadMockFactory = ArticleCreatePayloadMockFactory(category)
    }

    @Test
    fun testAllById() {
        articleCreatePayloadMockFactory.create()
            .let { articleRepository.create(articleCreatePayloadMapper.map(it)) }
        val count = articleRepository.count { withCategory(category) }

        mockMvc.get("/categories/${category.id}/articles")
            .andExpect {
                status { isOk() }
                header {
                    string("Total-Count", count.toString())
                    string("Total-Page", ceil(count / 20.0).toInt().toString())
                }
            }
            .andReturn()
    }

    @Test
    fun testAllByName() {
        articleCreatePayloadMockFactory.create()
            .let { articleRepository.create(articleCreatePayloadMapper.map(it)) }
        val count = articleRepository.count { withCategory(category) }

        mockMvc.get("/categories/@${category.name}/articles")
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
