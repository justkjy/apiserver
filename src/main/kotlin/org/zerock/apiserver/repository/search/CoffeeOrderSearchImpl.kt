package org.zerock.apiserver.repository.search

import org.springframework.data.domain.Page
import org.zerock.apiserver.domain.entity.CoffeeOrder
import org.zerock.apiserver.domain.dto.page.PageRequestDTO

class CoffeeOrderSearchImpl : CoffeeOrderSearch {

    override fun search1(pageRequestDTO: PageRequestDTO): Page<CoffeeOrder>? {
        TODO("Not yet implemented")
    }


}