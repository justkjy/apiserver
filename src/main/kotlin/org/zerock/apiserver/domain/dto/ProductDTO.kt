package org.zerock.apiserver.domain.dto

import org.springframework.web.multipart.MultipartFile
import kotlin.reflect.jvm.internal.impl.metadata.deserialization.Flags

data class ProductDTO(
    var pno : Long? = null,

    var pname : String? = null,

    var price : Int = 0,

    var pdesc : String = "",

    var delFlags: Boolean = true,

    var files:List<MultipartFile> = arrayListOf(),

    var uploadFileNames:MutableList<String> = mutableListOf(),
)
