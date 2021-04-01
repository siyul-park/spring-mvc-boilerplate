package io.github.siyual_park.domain.user

import io.github.siyual_park.domain.Mapper
import io.github.siyual_park.domain.scope.ScopeFetchExecutor
import io.github.siyual_park.model.scope.normalize
import io.github.siyual_park.model.user.User
import io.github.siyual_park.model.user.UserResponsePayload

class UserResponsePayloadMapper(
    private val scopeFetchExecutor: ScopeFetchExecutor,
    private val fetchScope: Boolean = true
) : Mapper<User, UserResponsePayload> {

    override fun map(input: User) = with(input) {
        UserResponsePayload(
            id!!,
            name,
            nickname,
            if (fetchScope) {
                scopeFetchExecutor.execute(input, 0).normalize()
            } else {
                null
            },
            createdAt!!,
            updatedAt
        )
    }
}
