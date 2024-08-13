package org.zerock.apiserver.domain.dto

import org.springframework.web.multipart.MultipartFile

data class ProductDTO(
    var pno : Long? = null,

    var pname : String? = null,

    var price : Int = 0,

    var pdesc : String = "",

    var delFlags: Boolean = true,

    var files:List<MultipartFile> = arrayListOf(),

    var uploadFileNames:MutableList<String> = mutableListOf(),
)
