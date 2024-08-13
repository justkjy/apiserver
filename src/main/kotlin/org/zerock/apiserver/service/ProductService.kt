package org.zerock.apiserver.service

import org.zerock.apiserver.domain.dto.ProductDTO
import org.zerock.apiserver.domain.dto.page.PageRequestDTO
import org.zerock.apiserver.domain.dto.page.PageResponseDTO

interface ProductService {
    fun register(productDTO: ProductDTO):Long

    fun getList(pageRequestDTO: PageRequestDTO): PageResponseDTO<ProductDTO>
    //fun getList(pageRequestDTO: PageRequestDTO)

    fun get(pno : Long) : ProductDTO

    fun modify(productDTO : ProductDTO)

    fun remove(pno: Long)
}