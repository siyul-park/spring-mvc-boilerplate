package io.github.siyual_park.repository.expansion

import javax.persistence.criteria.Path
import kotlin.reflect.KMutableProperty1

operator fun <X, T, V> Path<X>.get(property1: KMutableProperty1<T, V>): Path<V> = get(property1.name)
