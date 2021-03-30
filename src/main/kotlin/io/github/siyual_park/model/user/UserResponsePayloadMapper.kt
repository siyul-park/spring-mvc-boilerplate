package io.github.siyual_park.model.user

import io.github.siyual_park.model.Mapper
import org.springframework.stereotype.Component

@Component
class UserResponsePayloadMapper : Mapper<User, UserResponsePayload> {
    override fun map(input: User) = UserResponsePayload.from(input)
}
