package org.zerock.apiserver.domain.entity

import jakarta.persistence.*

@Entity
@Table(name="tbl_cafeproduct")
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var pno: Long? = null,

    var pname: String = "",

    var price: Int = 0,

    var pdesc: String = "",

    var delFlag: Boolean = false,

    @ElementCollection
    var cafeImageList: MutableList<ProductCafeImage> = mutableListOf(),

    ) {
    private fun addImage(image: ProductCafeImage) {
        image.ord = cafeImageList.size
        cafeImageList.add(image)
    }

    fun addImageString(fileName:String) {
        val productImage = ProductCafeImage(fileName = fileName)

        addImage(productImage)
    }

    fun clearList() {
        cafeImageList.clear()
    }

}