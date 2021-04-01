package io.github.siyual_park.domain.scope

import io.github.siyual_park.domain.Mapper
import io.github.siyual_park.model.scope.ScopeToken
import io.github.siyual_park.model.scope.ScopeTokenCreatePayload
import org.springframework.stereotype.Component

@Component
class ScopeTokenCreatePayloadMapper : Mapper<ScopeTokenCreatePayload, ScopeToken> {
    override fun map(input: ScopeTokenCreatePayload): ScopeToken {
        return ScopeToken(input.name, input.description)
    }
}
