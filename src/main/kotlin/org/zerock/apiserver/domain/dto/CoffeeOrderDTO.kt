package org.zerock.apiserver.domain.dto


import org.zerock.apiserver.domain.entity.CoffeeOrder

data class CoffeeOrderDTO (
    var id : Long? = null,
    var coffeeName: String = "",
    var description: String = "",
    var orderName : String = "",
    var orderCount: Int = 0,
    var createdDate: String? = null,
    var updatedDate: String? = null,

)

// update용
fun CoffeeOrderDTO.toCoffeeOrder(coffeeOrderDTO: CoffeeOrderDTO) : CoffeeOrder {
    return CoffeeOrder().apply {
        this.coffeeName = coffeeOrderDTO.coffeeName
        this.description = coffeeOrderDTO.description
        this.orderName = coffeeOrderDTO.orderName
        this.orderCount = coffeeOrderDTO.orderCount
        //val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        //this.createdDate = LocalDateTime.parse(coffeeOrderDTO.createdDate.toString(), pattern)
    }
}

// 요청 클라이언트 전달용
//fun CoffeeOrderDTO.toCoffeeOrderFull(coffeeOrderDTO: CoffeeOrderDTO) : CoffeeOrder {
//    return CoffeeOrder().apply {
//        this.id = coffeeOrderDTO.id
//        this.coffeeName = coffeeOrderDTO.coffeeName
//        this.description = coffeeOrderDTO.description
//        this.orderName = coffeeOrderDTO.orderName
//        this.orderCount = coffeeOrderDTO.orderCount
//        val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
//        this.updatedDate = LocalDateTime.parse(coffeeOrderDTO.updatedDate, pattern)
//    }
//}