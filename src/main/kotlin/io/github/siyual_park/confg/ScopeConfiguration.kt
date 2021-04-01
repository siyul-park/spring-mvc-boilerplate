package io.github.siyual_park.confg

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
        val accessTokenCreateScope = required(ScopeToken("create:access-token"))

        default.also {
            relation(it, accessTokenCreateScope)
        }
    }
}
