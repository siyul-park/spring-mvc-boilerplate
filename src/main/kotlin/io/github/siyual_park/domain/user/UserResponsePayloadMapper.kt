package io.github.siyual_park.domain.user

import io.github.siyual_park.model.Mapper
import io.github.siyual_park.model.user.User
import io.github.siyual_park.model.user.UserResponsePayload
import org.springframework.stereotype.Component

@Component
class UserResponsePayloadMapper : Mapper<User, UserResponsePayload> {
    override fun map(input: User) = UserResponsePayload.from(input)
}
