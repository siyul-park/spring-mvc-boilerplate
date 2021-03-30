package io.github.siyual_park.factory

import io.github.siyual_park.model.user.UserCreatePayload

class UserCreatePayloadMockFactory : MockFactory<UserCreatePayload> {
    private var count: Int = 0

    override fun create(): UserCreatePayload {
        val count = count++
        return UserCreatePayload(
            "${RandomFactory.createString(10)}-$count",
            "${RandomFactory.createString(10)}-$count",
            "${RandomFactory.createString(10)}-$count"
        )
    }
}
