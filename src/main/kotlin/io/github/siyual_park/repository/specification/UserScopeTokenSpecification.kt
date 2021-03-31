package io.github.siyual_park.repository.specification

import io.github.siyual_park.model.scope.UserScopeToken
import io.github.siyual_park.model.user.User
import io.github.siyual_park.repository.expansion.get
import org.springframework.data.jpa.domain.Specification

object UserScopeTokenSpecification {
    fun withUser(user: User) = withUser(user.id!!)

    fun withUser(userId: String) = Specification<UserScopeToken> { root, _, builder ->
        builder.equal(root[UserScopeToken::user][User::id], userId)
    }
}
