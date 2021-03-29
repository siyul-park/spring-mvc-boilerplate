package io.github.siyual_park.model

interface Mapper<I, O> {
    fun map(input: I): O
}
