package io.github.siyual_park.domain.scope

import io.github.siyual_park.model.Mapper
import io.github.siyual_park.model.scope.ScopeToken
import io.github.siyual_park.model.scope.ScopeTokenResponsePayload
import org.springframework.stereotype.Component

@Component
class ScopeResponsePayloadMapper(
    private val scopeFetchExecutor: ScopeFetchExecutor
) : Mapper<ScopeToken, ScopeTokenResponsePayload> {
    override fun map(input: ScopeToken) = with(input) {
        ScopeTokenResponsePayload(
            id!!,
            name,
            description,
            scopeFetchExecutor.execute(this, 1)
                .map { child ->
                    ScopeTokenResponsePayload(
                        child.id!!,
                        child.name,
                        child.description,
                        null,
                        child.createdAt!!,
                        child.updatedAt
                    )
                }
                .toSet(),
            createdAt!!,
            updatedAt
        )
    }
}
