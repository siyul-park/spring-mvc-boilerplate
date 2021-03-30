package io.github.siyual_park.domain.user

import io.github.siyual_park.domain.security.HashEncoder
import io.github.siyual_park.exception.BadRequestException
import io.github.siyual_park.model.user.User
import io.github.siyual_park.model.user.UserUpdatePayload
import io.github.siyual_park.repository.patch.JsonMergePatchFactory
import io.github.siyual_park.repository.patch.LambdaPatch
import io.github.siyual_park.repository.patch.Patch
import org.springframework.stereotype.Component

@Component
class UserPatchFactory(private val jsonMergePatchFactory: JsonMergePatchFactory) {
    fun create(payload: UserUpdatePayload): Patch<User> {
        val jsonMergePatch: Patch<User> = jsonMergePatchFactory.create(payload)

        return LambdaPatch.from {
            payload.password?.let {
                if (it.isPresent) {
                    password = HashEncoder.encode(it.get(), hashAlgorithm)
                        .let { bytes -> HashEncoder.bytesToHex(bytes) }
                } else {
                    throw BadRequestException()
                }
                payload.password = null
            }

            jsonMergePatch.apply(this)
        }
    }
}
