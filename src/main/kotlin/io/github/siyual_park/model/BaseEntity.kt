package io.github.siyual_park.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
open class BaseEntity {
    @field:Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: String? = null

    @field:CreatedDate
    @field:Column(updatable = false)
    var createdAt: LocalDateTime? = null

    @field:LastModifiedDate
    var updatedAt: LocalDateTime? = null
}
