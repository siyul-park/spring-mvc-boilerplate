package io.github.siyual_park.controller

import io.github.siyual_park.config.PreDefinedScope
import io.github.siyual_park.domain.user.UserCreateExecutor
import io.github.siyual_park.domain.user.UserCreatePayloadMapper
import io.github.siyual_park.domain.user.UserDeleteExecutor
import io.github.siyual_park.domain.user.UserPatchFactory
import io.github.siyual_park.domain.user.UserResponsePayloadMapper
import io.github.siyual_park.exception.AccessDeniedException
import io.github.siyual_park.expansion.has
import io.github.siyual_park.model.JsonView
import io.github.siyual_park.model.token.TokenAuthentication
import io.github.siyual_park.model.user.User
import io.github.siyual_park.model.user.UserCreatePayload
import io.github.siyual_park.model.user.UserResponsePayload
import io.github.siyual_park.model.user.UserUpdatePayload
import io.github.siyual_park.repository.UserRepository
import io.swagger.annotations.Api
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import springfox.documentation.annotations.ApiIgnore

@Api
@RestController
@RequestMapping("/users")
class UserController(
    private val userRepository: UserRepository,
    private val userCreatePayloadMapper: UserCreatePayloadMapper,
    private val userResponsePayloadMapper: UserResponsePayloadMapper,
    private val userCreateExecutor: UserCreateExecutor,
    private val userDeleteExecutor: UserDeleteExecutor,
    private val userPatchFactory: UserPatchFactory
) {

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody payload: UserCreatePayload): JsonView<UserResponsePayload> {
        return userCreateExecutor.execute(userCreatePayloadMapper.map(payload))
            .let { userResponsePayloadMapper.map(it) }
            .let { JsonView.of(it, UserResponsePayload.Private::class) }
    }

    @PreAuthorize("hasAuthority('${PreDefinedScope.User.update}')")
    @PatchMapping("/{user-id}")
    @ResponseStatus(HttpStatus.OK)
    fun update(
        @ApiIgnore authentication: TokenAuthentication,
        @PathVariable("user-id") id: String,
        @RequestBody payload: UserUpdatePayload
    ): JsonView<UserResponsePayload> {
        if (authentication.principal.id != id) {
            throw AccessDeniedException()
        }
        if (!authentication.authorities.has(PreDefinedScope.User.Scope.update)) {
            payload.scope = null
        }

        return userRepository.updateById(id, userPatchFactory.create(payload))
            .let { userResponsePayloadMapper.map(it) }
            .let { JsonView.of(it, UserResponsePayload.Private::class) }
    }

    @PreAuthorize("hasAuthority('${PreDefinedScope.User.read}')")
    @GetMapping("/{user-id}")
    @ResponseStatus(HttpStatus.OK)
    fun findById(
        @AuthenticationPrincipal user: User,
        @PathVariable("user-id") id: String
    ): JsonView<UserResponsePayload> {
        val view = if (user.id == id) {
            UserResponsePayload.Private::class
        } else {
            UserResponsePayload.Public::class
        }

        return userRepository.findByIdOrFail(id)
            .let { userResponsePayloadMapper.map(it) }
            .let { JsonView.of(it, view) }
    }

    @PreAuthorize("hasAuthority('${PreDefinedScope.User.read}')")
    @GetMapping("/@{user-name}")
    @ResponseStatus(HttpStatus.OK)
    fun findByName(
        @AuthenticationPrincipal user: User,
        @PathVariable("user-name") name: String
    ): JsonView<UserResponsePayload> {
        val view = if (user.name == name) {
            UserResponsePayload.Private::class
        } else {
            UserResponsePayload.Public::class
        }

        return userRepository.findByNameOrFail(name)
            .let { userResponsePayloadMapper.map(it) }
            .let { JsonView.of(it, view) }
    }

    @PreAuthorize("hasAuthority('${PreDefinedScope.User.delete}')")
    @DeleteMapping("/{user-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @AuthenticationPrincipal user: User,
        @PathVariable("user-id") id: String
    ) {
        if (user.id != id) {
            throw AccessDeniedException()
        }
        return userDeleteExecutor.execute(id)
    }
}
