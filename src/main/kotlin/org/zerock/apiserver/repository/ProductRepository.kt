package org.zerock.apiserver.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.zerock.apiserver.domain.entity.Product

interface ProductRepository : JpaRepository<Product, Long> {

    //@Query("select p, pi  from Product p left join p.cafeImageList pi where pi.ord = 0 and p.delFlag = false")
    @Query("select p  from Product p left join p.cafeImageList pi where pi.ord = 0 and p.delFlag = false")
    fun selectList(pageable: Pageable?): Page<Product>
}