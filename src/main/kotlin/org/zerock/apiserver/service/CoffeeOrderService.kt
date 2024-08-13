package org.zerock.apiserver.service


import org.zerock.apiserver.domain.dto.CoffeeOrderDTO
import org.zerock.apiserver.domain.entity.CoffeeOrder
import org.zerock.apiserver.domain.entity.toCoffeeOrderDTO
import org.zerock.apiserver.domain.dto.page.PageRequestDTO
import org.zerock.apiserver.domain.dto.page.PageResponseDTO

interface CoffeeOrderService {

    fun register(coffeeOrderDTO: CoffeeOrderDTO) : Long

    fun get(id: Long) : CoffeeOrderDTO

    fun modify(id: Long, coffeeOrderDTO: CoffeeOrderDTO) : CoffeeOrderDTO

    fun remove(id: Long)

    fun list(pageRequestDTO: PageRequestDTO) : PageResponseDTO<CoffeeOrderDTO>

    fun entityCoffeeOrderDTO(coffeeOrder: CoffeeOrder) : CoffeeOrderDTO {
        return coffeeOrder.toCoffeeOrderDTO(coffeeOrder)
    }
}