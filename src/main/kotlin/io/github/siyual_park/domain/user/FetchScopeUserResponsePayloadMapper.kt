package io.github.siyual_park.domain.user

import io.github.siyual_park.domain.scope.ScopeFetchExecutor
import io.github.siyual_park.model.scope.normalize
import io.github.siyual_park.model.user.User
import io.github.siyual_park.model.user.UserResponsePayload
import org.springframework.stereotype.Component

@Component
class FetchScopeUserResponsePayloadMapper(
    private val scopeFetchExecutor: ScopeFetchExecutor
) : UserResponsePayloadMapper {

    override fun map(input: User) = with(input) {
        UserResponsePayload(
            id!!,
            name,
            nickname,
            scopeFetchExecutor.execute(input, 0).normalize(),
            createdAt!!,
            updatedAt
        )
    }
}
