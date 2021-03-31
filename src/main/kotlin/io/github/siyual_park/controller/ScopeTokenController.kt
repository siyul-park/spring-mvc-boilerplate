package io.github.siyual_park.controller

import io.github.siyual_park.domain.scope.ScopeFetchExecutor
import io.github.siyual_park.domain.scope.ScopeResponsePayloadMapper
import io.github.siyual_park.domain.scope.ScopeTokenCreatePayloadMapper
import io.github.siyual_park.model.scope.ScopeTokenCreatePayload
import io.github.siyual_park.model.scope.ScopeTokenRelation
import io.github.siyual_park.model.scope.ScopeTokenResponsePayload
import io.github.siyual_park.repository.ScopeTokenRelationRepository
import io.github.siyual_park.repository.ScopeTokenRepository
import io.github.siyual_park.repository.UserScopeTokenRepository
import io.swagger.annotations.Api
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@Api
@RestController
@RequestMapping("/scope-tokens")
class ScopeTokenController(
    private val scopeTokenRepository: ScopeTokenRepository,
    private val scopeTokenRelationRepository: ScopeTokenRelationRepository,
    private val userScopeTokenRepository: UserScopeTokenRepository,
    private val scopeFetchExecutor: ScopeFetchExecutor,
    private val scopeResponsePayloadMapper: ScopeResponsePayloadMapper,
    private val scopeTokenCreatePayloadMapper: ScopeTokenCreatePayloadMapper,

) {

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody payload: ScopeTokenCreatePayload): ScopeTokenResponsePayload {
        val children = payload.children?.let {
            scopeTokenRepository.findAllByNameIn(it)
        } ?: emptyList()

        val scopeToken = scopeTokenCreatePayloadMapper.map(payload)
            .let { scopeTokenRepository.create(it) }
        children.forEach {
            scopeTokenRelationRepository.create(ScopeTokenRelation(scopeToken, it))
        }

        return scopeResponsePayloadMapper.map(scopeToken)
    }
}
