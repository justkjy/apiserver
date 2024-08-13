package org.zerock.apiserver.controller

import org.springframework.web.bind.annotation.*
import org.zerock.apiserver.domain.dto.CoffeeOrderDTO
import org.zerock.apiserver.domain.dto.page.PageRequestDTO
import org.zerock.apiserver.domain.dto.page.PageResponseDTO
import org.zerock.apiserver.logger
import org.zerock.apiserver.service.CoffeeOrderService
import kotlin.random.Random

@RestController
@RequestMapping("/api/order")
class CoffeeOrderController(
    private val coffeeOrderService: CoffeeOrderService
) {
    val log = logger()

    @GetMapping("/{id}")
    fun get(
        @PathVariable(name="id") id : Long
    ): CoffeeOrderDTO {
        return coffeeOrderService.get(id)
    }

    @GetMapping("/list")
    fun list(
        @RequestParam(value="page", defaultValue="1") page: Int,
        @RequestParam(value="size", defaultValue="1") size: Int,
    ): PageResponseDTO<CoffeeOrderDTO> {
        val pageRequestDTO = PageRequestDTO(page, size)
        println("page : $page, size : $size")

        return coffeeOrderService.list(pageRequestDTO)
    }

    @PostMapping("/")
    fun register(
        @RequestBody dto: CoffeeOrderDTO
    ) : Map<String, Long>{
        val id = coffeeOrderService.register(dto)

        return mapOf("id" to id)
    }

    @PutMapping("/{id}")
    fun modify(
        @PathVariable(name="id") id : Long,
        @RequestBody dto: CoffeeOrderDTO
    ) : Map<String, String> {
        coffeeOrderService.modify(id, dto)

        return mapOf("RESULT" to "SUCCESS")
    }

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable(name="id") id : Long
    ) : Map<String, String>{
        coffeeOrderService.remove(id)

        return mapOf("RESULT" to "SUCCESS")
    }

    @GetMapping("/testData")
    fun testRegister(
        @RequestParam(value="count") count: Long,
    ) {
        for(i in 1..count){
            val coffeeOrderDTO = when(i % 4) {
                1L -> {
                    CoffeeOrderDTO().apply {
                        this.coffeeName = String.format("아메리카노 $i")
                        this.description = "쓴맛"
                        this.orderCount = Random.nextInt(1, 10)
                        this.orderName = "just kim"
                    }
                }
                2L -> {

                    CoffeeOrderDTO().apply {
                        this.coffeeName = String.format("라데 $i")
                        this.description = "달달한 맛"
                        this.orderCount = Random.nextInt(1, 10)
                        this.orderName = "just kim"
                    }
                }
                3L -> {

                    CoffeeOrderDTO().apply {
                        this.coffeeName = String.format("콜드 브루 $i")
                        this.description = "고소하고 쓴 맛"
                        this.orderCount = Random.nextInt(1, 10)
                        this.orderName = "just kim"
                    }
                }
                else -> {
                    CoffeeOrderDTO().apply {
                        this.coffeeName = String.format("스무디 $i")
                        this.description = "고소하고 쓴 맛"
                        this.orderCount = Random.nextInt(1, 10)
                        this.orderName = "just kim"
                    }
                }
            }

            coffeeOrderService.register(coffeeOrderDTO)
        }
    }
}