package io.github.siyual_park.repository.expansion

import org.springframework.data.jpa.domain.Specification

operator fun <T> Specification<T>.not(): Specification<T> = Specification.not(this)

infix fun <T> Specification<T>.and(spec: Specification<T>?): Specification<T> = and(spec)
infix fun <T> Specification<T>.or(spec: Specification<T>?): Specification<T> = or(spec)
