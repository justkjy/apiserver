package org.zerock.apiserver.domain.entity

import jakarta.persistence.*
import org.zerock.apiserver.domain.dto.CoffeeOrderDTO
import java.time.format.DateTimeFormatter

@Entity
// table coffee_order
@Table(name = "tbl_coffee_ordr")
class CoffeeOrder(
    coffeeName: String = "",
    description: String = "",
    orderName : String = "",
    orderCount: Int = 0
) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(name = "coffee_name")
    var coffeeName: String = coffeeName

    var description: String = description

    @Column(name = "order_name")
    var orderName: String = orderName

    @Column(name = "order_count")
    var orderCount: Int = orderCount
}

fun CoffeeOrder.toCoffeeOrderDTO(coffeeOrder: CoffeeOrder): CoffeeOrderDTO {
    return CoffeeOrderDTO().apply {
        this.id = coffeeOrder.id ?: -1
        this.coffeeName = coffeeOrder.coffeeName
        this.description = coffeeOrder.description
        this.orderName = coffeeOrder.orderName
        this.orderCount = coffeeOrder.orderCount
        this.createdDate = coffeeOrder.createdDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        this.updatedDate = coffeeOrder.updatedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    }
}