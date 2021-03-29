package io.github.siyual_park.domain

import io.github.siyual_park.repository.CustomRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import java.util.stream.Stream

class Paginator<T : Any, ID>(
    private val repository: CustomRepository<T, ID>
) {
    fun query(
        offset: Int?,
        limit: Int?,
        sort: Sort = Sort.unsorted()
    ): ResponseEntity<Stream<T>> {
        val pageRequest = PageRequest.of(offset ?: 0, limit ?: 20, sort)
        val pageResponse = repository.findAll(pageRequest)

        val headers = HttpHeaders()
        headers["Total-Count"] = pageResponse.totalElements.toString()
        headers["Total-Page"] = pageResponse.totalPages.toString()

        return ResponseEntity.ok()
            .headers(headers)
            .body(pageResponse.get())
    }
}
