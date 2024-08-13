package org.zerock.apiserver.repository.search

import org.springframework.data.domain.Page
import org.zerock.apiserver.domain.entity.CoffeeOrder
import org.zerock.apiserver.domain.dto.page.PageRequestDTO

interface CoffeeOrderSearch {
    fun search1(
        pageRequestDTO: PageRequestDTO,
    ) : Page<CoffeeOrder>?
}