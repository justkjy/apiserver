package org.zerock.apiserver.domain.entity

import jakarta.persistence.Embeddable

@Embeddable
data class ProductCafeImage(
    var fileName:String = "",

    var ord: Int? = null
)