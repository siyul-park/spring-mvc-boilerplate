package io.github.siyual_park.model.scope

data class ScopeTokenCreatePayload(
    var name: String,
    var description: String?,

    var children: Set<String>?
)
