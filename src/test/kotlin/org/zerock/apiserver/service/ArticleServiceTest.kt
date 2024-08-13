package org.zerock.apiserver.service

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ArticleServiceTest {
}

data class SaveCoffeeOrderDTO(
    var coffeeName: String = "",
    var description: String = "",
    var orderName : String = "",
    var orderCount: Int = 0
) {
    var id : Long = 0
    var updatedDate: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
}

