package io.github.siyual_park.controller

import io.github.siyual_park.domain.scope.ScopeFetchExecutor
import io.github.siyual_park.domain.security.HashEncoder
import io.github.siyual_park.domain.security.TokenExchanger
import io.github.siyual_park.domain.security.TokenFactory
import io.github.siyual_park.exception.AccessDeniedException
import io.github.siyual_park.exception.UnauthorizedException
import io.github.siyual_park.model.scope.has
import io.github.siyual_park.model.token.TokenResponsePayload
import io.github.siyual_park.model.user.User
import io.github.siyual_park.property.TokenProperty
import io.github.siyual_park.repository.UserRepository
import io.swagger.annotations.Api
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@Api
@RestController
@RequestMapping("/token")
class TokenController(
    private val userRepository: UserRepository,
    private val scopeFetchExecutor: ScopeFetchExecutor,
    private val tokenExchanger: TokenExchanger,
    private val tokenFactory: TokenFactory,
    private val tokenProperty: TokenProperty
) {

    @PostMapping("", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun create(@RequestParam username: String, @RequestParam password: String): TokenResponsePayload {
        val user = userRepository.findByNameOrFail(username)
        validateCanCreate(user, password)

        val token = tokenFactory.create(user, tokenProperty.expiresIn)
        val accessToken = tokenExchanger.encode(token)

        return TokenResponsePayload(
            accessToken,
            "bearer",
            tokenProperty.expiresIn
        )
    }

    private fun validateCanCreate(user: User, password: String) {
        val hashedPassword = HashEncoder.encode(password, user.hashAlgorithm)
            .let { HashEncoder.bytesToHex(it) }
        if (user.password != hashedPassword) {
            throw UnauthorizedException("Password incorrect")
        }
        scopeFetchExecutor.execute(user).let {
            if (!it.has("create:access-token")) {
                throw AccessDeniedException("Not have create:access-token scope")
            }
        }
    }
}
