package io.github.siyual_park.model.user

import io.github.siyual_park.model.UpdatePayload
import java.util.Optional

data class UserUpdatePayload(
    var name: Optional<String>? = null,
    var nickname: Optional<String>? = null,
    var password: Optional<String>? = null,
    var scope: Optional<String>? = null,
) : UpdatePayload
