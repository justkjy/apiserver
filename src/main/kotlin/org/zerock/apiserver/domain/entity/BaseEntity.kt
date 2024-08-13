package org.zerock.apiserver.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

@MappedSuperclass
abstract class BaseEntity {
    @CreatedDate
    @Column(nullable = false, updatable = false, name = "created_date")
    var createdDate: LocalDateTime = LocalDateTime.now()

    @LastModifiedDate
    @Column(nullable = false, updatable = true, name = "updated_date")
    var updatedDate: LocalDateTime = LocalDateTime.now()
}
