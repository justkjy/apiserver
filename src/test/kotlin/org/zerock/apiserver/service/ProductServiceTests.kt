package org.zerock.apiserver.service

import io.kotest.core.spec.style.StringSpec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.zerock.apiserver.domain.dto.ProductDTO
import java.util.*

@SpringBootTest
class ProductServiceTests(
    @Autowired val productService: ProductService, body: StringSpec.() -> Unit
): StringSpec({
    "Save" {
//        val productDTO = ProductDTO(
//            pname = "새로운 상품",
//            pdesc = "신규 추가 상품",
//            price = 100
//        )
//
//        productDTO.uploadFileNames.add(UUID.randomUUID().toString() + "_" + "Test1.jpg")
//        productDTO.uploadFileNames.add(UUID.randomUUID().toString() + "_" + "Test2.jpg")
//
//        productService.register(productDTO)
    }

})