package io.github.siyual_park.domain.user

import io.github.siyual_park.domain.Mapper
import io.github.siyual_park.model.user.User
import io.github.siyual_park.model.user.UserResponsePayload

interface UserResponsePayloadMapper : Mapper<User, UserResponsePayload>
