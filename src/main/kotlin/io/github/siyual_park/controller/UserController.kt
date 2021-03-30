package io.github.siyual_park.controller

import io.github.siyual_park.model.user.UserCreatePayload
import io.github.siyual_park.model.user.UserCreatePayloadMapper
import io.github.siyual_park.model.user.UserResponsePayload
import io.github.siyual_park.model.user.UserResponsePayloadMapper
import io.github.siyual_park.model.user.UserUpdatePayload
import io.github.siyual_park.repository.UserRepository
import io.github.siyual_park.repository.patch.JsonMergePatchFactory
import io.swagger.annotations.Api
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@Api
@RestController
@RequestMapping("/users")
class UserController(
    private val userRepository: UserRepository,
    private val userCreatePayloadMapper: UserCreatePayloadMapper,
    private val userResponsePayloadMapper: UserResponsePayloadMapper,
    private val jsonMergePatchFactory: JsonMergePatchFactory
) {

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody payload: UserCreatePayload): UserResponsePayload {
        return userRepository.create(userCreatePayloadMapper.map(payload))
            .let { userResponsePayloadMapper.map(it) }
    }

    @PatchMapping("/{user-id}")
    @ResponseStatus(HttpStatus.OK)
    fun update(
        @PathVariable("user-id") id: String,
        @RequestBody payload: UserUpdatePayload
    ): UserResponsePayload {
        return userRepository.updateById(id, jsonMergePatchFactory.create(payload))
            .let { userResponsePayloadMapper.map(it) }
    }

    @GetMapping("/{user-id}")
    @ResponseStatus(HttpStatus.OK)
    fun findById(@PathVariable("user-id") id: String): UserResponsePayload {
        return userRepository.findByIdOrFail(id)
            .let { userResponsePayloadMapper.map(it) }
    }

    @GetMapping("/@{user-name}")
    @ResponseStatus(HttpStatus.OK)
    fun findByName(@PathVariable("user-name") name: String): UserResponsePayload {
        return userRepository.findByNameOrFail(name)
            .let { userResponsePayloadMapper.map(it) }
    }

    @DeleteMapping("/{user-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable("user-id") id: String) {
        return userRepository.deleteById(id)
    }
}
