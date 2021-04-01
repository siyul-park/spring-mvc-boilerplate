package io.github.siyual_park.config

object PreDefinedScope {
    object AccessToken {
        const val create = "create:access-token"
    }

    object User {
        const val read = "read:user"
        const val update = "update:user"
        const val delete = "delete:user"

        object Scope {
            const val update = "update:user.scope"
        }
    }

    object Category {
        const val create = "create:category"
        const val read = "read:category"
        const val update = "update:category"
        const val delete = "delete:category"
    }
}
