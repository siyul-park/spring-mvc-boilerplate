package io.github.siyual_park.domain

import io.github.siyual_park.exception.BadRequestException
import io.github.siyual_park.model.Mapper
import io.github.siyual_park.repository.CustomRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import java.util.stream.Stream

class Paginator<T : Any, ID, R>(
    private val repository: CustomRepository<T, ID, *>,
    private val mapper: Mapper<T, R>
) {
    fun query(
        offset: Int? = null,
        limit: Int? = null,
        spec: Specification<T>? = null,
        sort: Sort = Sort.unsorted()
    ): ResponseEntity<Stream<R>> {
        val finalOffset = offset ?: 0
        val finalLimit = limit ?: 20

        if (finalOffset < 0 || finalLimit <= 0) {
            throw BadRequestException()
        }

        val pageRequest = PageRequest.of(finalOffset, finalLimit, sort)
        val pageResponse = if (spec != null) {
            repository.findAll(spec, pageRequest)
        } else {
            repository.findAll(pageRequest)
        }

        val headers = HttpHeaders()
        headers["Total-Count"] = pageResponse.totalElements.toString()
        headers["Total-Page"] = pageResponse.totalPages.toString()

        val body = pageResponse.get()
            .map { mapper.map(it) }

        return ResponseEntity.ok()
            .headers(headers)
            .body(body)
    }

    companion object {
        fun <T : Any, ID> from(repository: CustomRepository<T, ID, *>) = Paginator<T, ID, T>(
            repository,
            object : Mapper<T, T> {
                override fun map(input: T): T = input
            }
        )

        fun <T : Any, ID, R> of(repository: CustomRepository<T, ID, *>, mapper: Mapper<T, R>) = Paginator(
            repository,
            mapper
        )
    }
}
