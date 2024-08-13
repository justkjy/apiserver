package org.zerock.apiserver.service

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ClassPathResource
import org.springframework.jdbc.datasource.init.ScriptUtils
import org.springframework.test.context.ActiveProfiles
import org.zerock.apiserver.domain.dto.CoffeeOrderDTO
import javax.sql.DataSource
import org.zerock.apiserver.domain.dto.page.PageRequestDTO

@SpringBootTest
@ActiveProfiles("test")
class CoffeeOrderImplTest(
    @Autowired val coffeeOrderService: CoffeeOrderService,
    @Autowired private val dataSource: DataSource
) : StringSpec({

    beforeSpec {
        println(">> initialize db")
        val script = ClassPathResource("db-init/test.sql")

        val dataSourceBuilder = DataSourceBuilder.create()
            .driverClassName("org.h2.Driver")
            .url("jdbc:h2:mem:coffee;MODE=MYSQL")
            .username("SA")
            .password("")
            .build()

//        dataSource.connection.use { connection ->
//            ScriptUtils.executeSqlScript(connection, script)
//        }
//
        dataSourceBuilder.connection.use{ connection->
            ScriptUtils.executeSqlScript(connection, script)
        }

    }

    "save" {
        for(i in 1..50) {
            val order = SaveCoffeeOrderDTO(
                coffeeName = "아메리카노",
                description = "시큼한 맛",
                orderName = "just kim",
                orderCount = 0
            )

            coffeeOrderService.register(CoffeeOrderDTO().apply {
                coffeeName = order.coffeeName
                description = order.description
                orderName= order.orderName
                orderCount = order.orderCount
            }).let {
                it shouldBe i
            }
        }
    }

    "update" {
        val order = SaveCoffeeOrderDTO(
            coffeeName = "라떼",
            description = "고소한맛",
            orderName = "저스트 킴",
            orderCount = 4
        )

        coffeeOrderService.modify(1, CoffeeOrderDTO().apply {
            coffeeName = order.coffeeName
            description = order.description
            orderName= order.orderName
            orderCount = order.orderCount
        }).let{
            it.id shouldBe  1L
            it.coffeeName shouldBe order.coffeeName
            it.orderName shouldBe order.orderName
            it.orderCount shouldBe 4
        }
    }

    "get by id" {
        val body = coffeeOrderService.get(1)
        println(body)
        body.id shouldBe 1
        body.orderCount shouldBe 4
    }

    "get list to page" {
        val pageResponsDTO = PageRequestDTO(page = 1)

        //coffeeOrderService.list(pageResponsDTO)
        coffeeOrderService.list(pageResponsDTO)
    }

})