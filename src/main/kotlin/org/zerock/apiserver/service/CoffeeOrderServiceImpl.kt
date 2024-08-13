package org.zerock.apiserver.service

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.zerock.apiserver.domain.dto.CoffeeOrderDTO
import org.zerock.apiserver.domain.entity.toCoffeeOrderDTO
import org.zerock.apiserver.domain.dto.page.PageRequestDTO
import org.zerock.apiserver.domain.dto.page.PageResponseDTO
import org.zerock.apiserver.domain.dto.toCoffeeOrder
import org.zerock.apiserver.logger
import org.zerock.apiserver.repository.CoffeeOrderRepository
import java.util.stream.Collectors


@Service
@Transactional
class CoffeeOrderServiceImpl(
    private val coffeeOrderRepository: CoffeeOrderRepository
) : CoffeeOrderService {

    @Transactional
    override fun register(coffeeOrderDTO: CoffeeOrderDTO): Long {
        val log = logger()
        log.info(".................")
        val coffeeOrder = coffeeOrderDTO.toCoffeeOrder(coffeeOrderDTO)
        val savedCoffeeOrder = coffeeOrderRepository.save(coffeeOrder)
        return savedCoffeeOrder.id ?: return -1

    }

    override fun get(id: Long): CoffeeOrderDTO {
        val coffeeOrder = coffeeOrderRepository.findByIdOrNull(id) ?: throw IllegalArgumentException("coffeeOrder not found")

        val log = logger()
        log.info("get.....................")
        val coffeeOrderDTO = coffeeOrder.toCoffeeOrderDTO(coffeeOrder)
        log.info(coffeeOrderDTO.toString())
        return coffeeOrderDTO
    }

    @Transactional
    override fun modify(id: Long, coffeeOrderDTO: CoffeeOrderDTO): CoffeeOrderDTO {
        val log = logger()
        log.info("modify.....................")
        val size = coffeeOrderRepository.count()
        log.info("size is $size")
        log.info(coffeeOrderDTO.toString())

//        coffeeOrderRepository.findById(id)
//            .orElseThrow { RuntimeException("coffee order id not found!") }

        val coffeeOrder = coffeeOrderDTO.toCoffeeOrder(coffeeOrderDTO)
        coffeeOrder.id = id
        val coffeeSave =  coffeeOrderRepository.save(coffeeOrder)

        return coffeeSave.toCoffeeOrderDTO(coffeeOrder)
    }

    @Transactional
    override fun remove(id: Long) {
        coffeeOrderRepository.deleteById(id)
    }

    override fun list(pageRequestDTO: PageRequestDTO): PageResponseDTO<CoffeeOrderDTO> {
        val log = logger()
        log.info("page list.....................")
        val pageable : Pageable =
            PageRequest.of(
                pageRequestDTO.page - 1, // 1페이지가 0이므로 주의
                pageRequestDTO.size,
                Sort.by(Sort.Direction.DESC, "id")
            )

        val result = coffeeOrderRepository.findAll(pageable)

        val dtoList =  result.content.stream()
            .map { coffeeOrder ->
                entityCoffeeOrderDTO(coffeeOrder)
            }
            .collect(Collectors.toList())

        val totalCount : Int = result.totalElements.toInt()

        val totalPage: Int = result.totalPages

        //val responseDTO = PageResponseDTO(dtoList, pageRequestDTO, totalCount)
        val responseDTO = PageResponseDTO(dtoList = dtoList,
            pageRequestDTO = pageRequestDTO,
            totalCount = totalCount,
            totalPage = totalPage,
            current = pageRequestDTO.page
        )

        responseDTO.pageResponseInit(pageRequestDTO)
        return responseDTO
    }
}