package org.zerock.apiserver.service

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.zerock.apiserver.domain.dto.ProductDTO
import org.zerock.apiserver.domain.dto.page.PageRequestDTO
import org.zerock.apiserver.domain.dto.page.PageResponseDTO
import org.zerock.apiserver.domain.entity.Product
import org.zerock.apiserver.domain.entity.ProductCafeImage
import org.zerock.apiserver.logger
import org.zerock.apiserver.repository.ProductRepository
import java.util.*

@Service
@Transactional
class ProductServiceImpl (
    private val productRepository: ProductRepository
): ProductService {

    override fun register(productDTO: ProductDTO): Long {
        val product = dtoToEntity(productDTO)
        val result : Product = productRepository.save(product)

        logger().info("register -----------\n $product ------------------------------------")

        product.cafeImageList.map { image-> image.fileName}
            .forEach { logger().info(it)}
        return result.pno ?: -1
    }

    override fun getList(pageRequestDTO: PageRequestDTO): PageResponseDTO<ProductDTO> {
        val pageable = PageRequest.of(
            pageRequestDTO.page -1,  //첫 페이지가 0.
            pageRequestDTO.size,
            Sort.by("pno").descending()
        )

        val result = productRepository.selectList(pageable)

        val productList = result.content
        val productDTOList = mutableListOf<ProductDTO>()
        for(list in productList) {
            val productDTO = ProductDTO().apply {
                this.pno = list.pno
                this.pname = list.pname
                this.pdesc = list.pdesc
                this.price = list.price
                list.cafeImageList.forEach { image ->
                    this.uploadFileNames.add(image.fileName)
                }
            }
            productDTOList.add(productDTO)
        }


        val totalCount = result.totalElements

        val totalPage: Int = result.totalPages

        val responseDTO = PageResponseDTO(dtoList = productDTOList,
            pageRequestDTO = pageRequestDTO,
            totalCount = totalCount.toInt(),
            totalPage = totalPage,
            current = pageRequestDTO.page
        )
        responseDTO.pageResponseInit(pageRequestDTO)
        return responseDTO

    }

    override fun get(pno: Long): ProductDTO {
        val result : Optional<Product> = productRepository.findById(pno)

        val product : Product = result.orElse(Product())

        logger().info("get--------------\n $product-----------------------------------")
        return entityToDTO(product)
    }

    override fun modify(productDTO : ProductDTO) {
        val result = productRepository.findById(productDTO.pno!!)

        val product = result.orElse(Product())

        // 기본 정보 등록
        product.pname = productDTO.pname ?:  ""

        product.pdesc = productDTO.pdesc

        product.price = productDTO.price

        product.clearList()

        val uploadFileNames : List<String> = productDTO.uploadFileNames

//        if(uploadFileNames.isNotEmpty()) {
//            uploadFileNames.stream().map{ uploadName ->
//                product.addImageString(uploadName)
//            }
//        }
        if(uploadFileNames.isNotEmpty()) {
            for (fileName in uploadFileNames) {
                product.addImageString(fileName)
            }
        }

        // db 저장
        productRepository.save(product)
    }

    override fun remove(pno: Long) {
        productRepository.deleteById(pno)
    }

    fun entityToDTO(product: Product): ProductDTO {
        val productDTO = ProductDTO().apply {
            this.pno = product.pno
            this.pname = product.pname
            this.pdesc = product.pdesc
            this.price = product.price

        }
        val imageList: List<ProductCafeImage> = product.cafeImageList

        if(imageList.isEmpty()) {
            return productDTO
        }

        val fileNameList : List<String> = imageList.stream().map { productImage->
            productImage.fileName

        }.toList()

        productDTO.uploadFileNames.addAll(fileNameList)

        return productDTO
    }

    fun dtoToEntity(productDTO: ProductDTO): Product {

        val product = Product().apply {
            this.pno = productDTO.pno
            this.pname = productDTO.pname ?:""
            this.pdesc = productDTO.pdesc
            this.price = productDTO.price

        }

        val uploadFileNames = productDTO.uploadFileNames

        if ( uploadFileNames.isEmpty()) {
            return product
        }

        uploadFileNames.map { fileName->
            product.addImageString(fileName)
        }

        return product
    }



}