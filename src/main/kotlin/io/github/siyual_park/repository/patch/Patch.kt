package io.github.siyual_park.repository.patch

interface Patch<T> {
    fun apply(obj: T): T
}
