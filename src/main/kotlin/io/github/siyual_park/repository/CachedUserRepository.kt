package io.github.siyual_park.repository

import io.github.siyual_park.model.user.User
import io.github.siyual_park.repository.base.SimpleCachedByIdRepository
import org.springframework.stereotype.Component

@Component
class CachedUserRepository(
    repository: UserRepository
) : SimpleCachedByIdRepository<User, String>(repository)
