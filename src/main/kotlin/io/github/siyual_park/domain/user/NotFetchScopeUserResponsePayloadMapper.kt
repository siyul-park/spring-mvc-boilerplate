package io.github.siyual_park.domain.user

import io.github.siyual_park.model.user.User
import io.github.siyual_park.model.user.UserResponsePayload
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component

@Component
class NotFetchScopeUserResponsePayloadMapper : UserResponsePayloadMapper {

    @Cacheable("NotFetchScopeUserResponsePayloadMapper.map")
    override fun map(input: User) = with(input) {
        UserResponsePayload(
            id!!,
            name,
            nickname,
            null,
            createdAt!!,
            updatedAt
        )
    }
}
