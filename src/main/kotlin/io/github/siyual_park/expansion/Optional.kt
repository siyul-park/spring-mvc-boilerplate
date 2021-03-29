package io.github.siyual_park.expansion

import java.util.Optional

fun <T> Optional<T>.toNullable(): T? = when (isPresent) {
    true -> get()
    false -> null
}
