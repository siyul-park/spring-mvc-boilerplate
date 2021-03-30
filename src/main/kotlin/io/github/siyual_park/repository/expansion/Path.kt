package io.github.siyual_park.repository.expansion

import javax.persistence.criteria.Path
import kotlin.reflect.KMutableProperty1

operator fun <X, V> Path<X>.get(property1: KMutableProperty1<X, V>): Path<V> = get(property1.name)
