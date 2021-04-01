package io.github.siyual_park.config

import io.github.siyual_park.domain.scope.ScopePrepareManager
import io.github.siyual_park.model.scope.ScopeToken
import io.github.siyual_park.property.ScopeProperty
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct

@Configuration
class ScopeConfiguration(
    private val scopePrepareManager: ScopePrepareManager,
    private val scopeProperty: ScopeProperty
) {
    @PostConstruct
    fun prepare() = with(scopePrepareManager) {
        val default = required(ScopeToken(scopeProperty.default))

        val accessTokenCreate = required(ScopeToken(PreDefinedScope.AccessToken.create))

        val userRead = required(ScopeToken(PreDefinedScope.User.read))
        val userUpdate = required(ScopeToken(PreDefinedScope.User.update))
        val userDelete = required(ScopeToken(PreDefinedScope.User.delete))

        val userScopeUpdate = required(ScopeToken(PreDefinedScope.User.Scope.update))

        val categoryCreate = required(ScopeToken(PreDefinedScope.Category.create))
        val categoryRead = required(ScopeToken(PreDefinedScope.Category.read))
        val categoryUpdate = required(ScopeToken(PreDefinedScope.Category.update))
        val categoryDelete = required(ScopeToken(PreDefinedScope.Category.delete))

        default.also {
            relation(it, accessTokenCreate)

            relation(it, userRead)
            relation(it, userUpdate)
            relation(it, userDelete)

            relation(it, categoryCreate)
            relation(it, categoryRead)
            relation(it, categoryUpdate)
            relation(it, categoryDelete)

            relation(it, userScopeUpdate)
        }
    }
}
