package org.zerock.apiserver.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.zerock.apiserver.domain.entity.CoffeeOrder

//interface CoffeeOrderRepository : JpaRepository<CoffeeOrder, Long>, CoffeeOrderSearch {
//}

interface CoffeeOrderRepository : JpaRepository<CoffeeOrder, Long> {
}