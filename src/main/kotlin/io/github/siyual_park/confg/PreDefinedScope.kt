package io.github.siyual_park.confg

interface CreateResourceScope {
    val create: String
}

object PreDefinedScope {
    val accessToken = object : CreateResourceScope {
        override val create: String
            get() = "create:access-token"
    }
}
