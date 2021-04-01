package io.github.siyual_park.domain.user

import io.github.siyual_park.domain.Mapper
import io.github.siyual_park.domain.security.HashEncoder
import io.github.siyual_park.model.user.User
import io.github.siyual_park.model.user.UserCreatePayload
import org.springframework.stereotype.Component

@Component
class UserCreatePayloadMapper : Mapper<UserCreatePayload, User> {
    override fun map(input: UserCreatePayload) = with(input) {
        User(
            name,
            nickname,
            HashEncoder.encode(password, User.defaultHashAlgorithm)
                .let { HashEncoder.bytesToHex(it) }
        )
    }
}
