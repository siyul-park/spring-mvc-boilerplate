package io.github.siyual_park.domain

interface Mapper<I, O> {
    fun map(input: I): O
}
