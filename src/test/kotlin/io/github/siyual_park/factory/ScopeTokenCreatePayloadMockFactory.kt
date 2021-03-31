package io.github.siyual_park.factory

import io.github.siyual_park.model.scope.ScopeTokenCreatePayload

class ScopeTokenCreatePayloadMockFactory : MockFactory<ScopeTokenCreatePayload> {
    private var count: Int = 0

    override fun create(): ScopeTokenCreatePayload {
        val count = count++
        return ScopeTokenCreatePayload(
            "${RandomFactory.createString(10)}-$count",
            "${RandomFactory.createString(10)}-$count"
        )
    }
}
