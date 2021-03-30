package io.github.siyual_park.model.user

import io.github.siyual_park.model.Mapper
import org.springframework.stereotype.Component

@Component
class UserCreatePayloadMapper : Mapper<UserCreatePayload, User> {
    override fun map(input: UserCreatePayload) = with(input) {
        User(name, nickname, password)
    }
}
