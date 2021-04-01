package io.github.siyual_park.model

import io.github.siyual_park.model.user.User
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

open class Resource(
    @ManyToOne
    @JoinColumn(name = "owner_id")
    val owner: User? = null
) : BaseEntity()
