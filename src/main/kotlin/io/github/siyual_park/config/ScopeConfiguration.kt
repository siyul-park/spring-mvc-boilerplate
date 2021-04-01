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
        val userScopeUpdate = required(ScopeToken(PreDefinedScope.User.Scope.update))

        default.also {
            relation(it, accessTokenCreate)
            relation(it, userScopeUpdate)
        }
    }
}
