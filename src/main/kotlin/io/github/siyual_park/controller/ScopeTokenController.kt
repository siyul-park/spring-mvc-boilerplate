package io.github.siyual_park.controller

import io.github.siyual_park.domain.Paginator
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
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.stream.Stream

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

    private val paginator = Paginator.of(scopeTokenRepository, scopeResponsePayloadMapper)

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody payload: ScopeTokenCreatePayload): ScopeTokenResponsePayload {
        val children = payload.children?.let {
            scopeTokenRepository.findAllByIdIn(it)
        } ?: emptyList()

        val scopeToken = scopeTokenCreatePayloadMapper.map(payload)
            .let { scopeTokenRepository.create(it) }
        children.forEach {
            scopeTokenRelationRepository.create(ScopeTokenRelation(scopeToken, it))
        }

        return scopeResponsePayloadMapper.map(scopeToken)
    }

    @GetMapping("")
    fun findAll(
        @RequestParam("page", required = false) offset: Int?,
        @RequestParam("per_page", required = false) limit: Int?,
        @RequestParam("sort", required = false) property: String?,
        @RequestParam("order", required = false) direction: Sort.Direction?
    ): ResponseEntity<Stream<ScopeTokenResponsePayload>> {
        return paginator.query(
            offset = offset,
            limit = limit,
            sort = Sort.by(
                direction ?: Sort.Direction.ASC,
                property ?: "id"
            )
        )
    }

    @GetMapping("/{scope-token-id}")
    @ResponseStatus(HttpStatus.OK)
    fun findById(@PathVariable("scope-token-id") id: String): ScopeTokenResponsePayload {
        return scopeTokenRepository.findByIdOrFail(id)
            .let { scopeResponsePayloadMapper.map(it) }
    }

    @GetMapping("/@{scope-token-name}")
    @ResponseStatus(HttpStatus.OK)
    fun findByName(@PathVariable("scope-token-name") name: String): ScopeTokenResponsePayload {
        return scopeTokenRepository.findByNameOrFail(name)
            .let { scopeResponsePayloadMapper.map(it) }
    }
}
