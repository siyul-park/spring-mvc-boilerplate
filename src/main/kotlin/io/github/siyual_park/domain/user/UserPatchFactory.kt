package io.github.siyual_park.domain.user

import io.github.siyual_park.domain.scope.ScopeFetchExecutor
import io.github.siyual_park.domain.security.HashEncoder
import io.github.siyual_park.exception.BadRequestException
import io.github.siyual_park.expansion.toNullable
import io.github.siyual_park.model.user.User
import io.github.siyual_park.model.user.UserUpdatePayload
import io.github.siyual_park.repository.patch.JsonMergePatchFactory
import io.github.siyual_park.repository.patch.LambdaPatch
import io.github.siyual_park.repository.patch.Patch
import org.springframework.stereotype.Component

@Component
class UserPatchFactory(
    private val jsonMergePatchFactory: JsonMergePatchFactory,
    private val scopeFetchExecutor: ScopeFetchExecutor,
    private val userScopeUpdateExecutor: UserScopeUpdateExecutor
) {
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

            payload.scope?.let {
                val scope = it.toNullable()?.let { value ->
                    scopeFetchExecutor.execute(value, 0)
                }
                userScopeUpdateExecutor.execute(this, scope)
                payload.scope = null
            }

            jsonMergePatch.apply(this)
        }
    }
}
