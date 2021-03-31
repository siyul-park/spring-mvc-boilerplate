package io.github.siyual_park.model.scope

data class ScopeTokenCreatePayload(
    var name: String,
    var description: String? = null,

    var children: Set<String>? = null
)
