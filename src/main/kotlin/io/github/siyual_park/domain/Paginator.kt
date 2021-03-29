package io.github.siyual_park.domain

import io.github.siyual_park.exception.BadRequestException
import io.github.siyual_park.repository.CustomRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import java.util.stream.Stream

class Paginator<T : Any, ID>(
    private val repository: CustomRepository<T, ID, *>
) {
    fun query(
        offset: Int? = null,
        limit: Int? = null,
        spec: Specification<T>? = null,
        sort: Sort = Sort.unsorted()
    ): ResponseEntity<Stream<T>> {
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

        return ResponseEntity.ok()
            .headers(headers)
            .body(pageResponse.get())
    }
}
