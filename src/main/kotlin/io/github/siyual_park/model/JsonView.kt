package io.github.siyual_park.model

import kotlin.reflect.KClass

data class JsonView<T>(
    val value: T,
    val view: KClass<*>? = null
) {
    companion object {
        fun <T : Any> of(value: T, view: KClass<*>?) = JsonView(value, view)
    }
}
