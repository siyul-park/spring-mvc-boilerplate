package io.github.siyual_park.repository.specification

import io.github.siyual_park.model.user.User
import io.github.siyual_park.repository.expansion.get
import org.springframework.data.jpa.domain.Specification

object UserSpecification {
    fun withName(name: String) = Specification<User> { root, _, builder ->
        builder.equal(root[User::name], name)
    }
}
