package org.zerock.apiserver.controller

import org.springframework.core.io.Resource
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.zerock.apiserver.domain.dto.ProductDTO
import org.zerock.apiserver.domain.dto.page.PageRequestDTO
import org.zerock.apiserver.domain.dto.page.PageResponseDTO
import org.zerock.apiserver.service.ProductService
import org.zerock.apiserver.util.CustomFileUtil

@RestController
@RequestMapping("/api/products")
class ProductController(
    private val fileUtil: CustomFileUtil,
    private val productService: ProductService
) {

    @GetMapping("/view/{fileName}")
    fun viewFileGet(
        @PathVariable("fileName") fileName:String
    ) : ResponseEntity<Resource> {
        return fileUtil.getFile(fileName)
    }

    // jwt 6차 -2 @PreAuthorize 어떤 권한을 가진 사람을 허용할껀지 결정
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping("/list")
    fun list(
        pageRequestDTO : PageRequestDTO
    ): PageResponseDTO<ProductDTO> {

        return productService.getList(pageRequestDTO)
    }

    @PostMapping("/")
    fun register(
        productDTO: ProductDTO
    ) : Map<String, Long> {
        val files:List<MultipartFile>  = productDTO.files

        // 문자 목록을 만들어 줘야 한다.
        val uploadFileNames:List<String> = fileUtil.saveFiles(files) ?: kotlin.run{
            emptyList()
        }

        productDTO.uploadFileNames = mutableListOf<String>().let {
            it.addAll(uploadFileNames)
            it
        }

        val pno: Long = productService.register(productDTO)


        return mapOf("result" to pno)
    }

    @GetMapping("/{pno}")
    fun read(
        @PathVariable("pno") pno: Long
    ) : ProductDTO {
        return productService.get(pno)
    }

    ////////////////////////////////////////
    // A B C  라는 파일이 있는데 A B는 남아 있고 D라는 파일이 생성(C라는 파일이 삭제
    // 1 ~ 2까지는 A B C 에 D라는 파일을 추가하는 작업
    /////////////////////////////////////////
    // 이건 오래된 파일
    // 수정할 파일의 id 값으로 이미 지정된 파일 획득
    @PutMapping("/{pno}")
    fun modify(
        @PathVariable("pno") pno: Long,
        productDTO: ProductDTO
    ) : Map<String, String> {

        // 1차 수정할 id 획득
        productDTO.pno = pno

        // 2차 pno 값으로 저장된 데이터를 획득 차후 수정해서 저장한다.
        val oldProductDTO = productService.get(pno)

        // 3차 새롭게 추가된 파일은 아래 변수에 저장하고 upload 저장 폴더에 새롭게 추가된 파일을 저장한다.
        var files:List<MultipartFile> = productDTO.files
        val currentUploadFileNames:List<String> = fileUtil.saveFiles(files) ?: kotlin.run{ listOf()}

        // 4차 계속 유지 되고 있는 파일
        // 기존에 있던 파일 획득, 주의!! 사용자가 삭제한 파일은 아래 productDTO에 없음
        var uploadFileNames = productDTO.uploadFileNames

        // 5차 3차에서 획득한 새롭게 추가된 파일을 4차에서 생성된 uploadFileName에 추가한다.
        if(currentUploadFileNames.isNotEmpty()){
            currentUploadFileNames.map { fileName ->
                uploadFileNames.add(fileName)
            }
        }

        // 6차 수정된 productDTO를 저장한다.
        productService.modify(productDTO)

        // 7차 productDTO에 삭제된 파일은 upload 파일에서 삭제
        val oldFileNames = oldProductDTO.uploadFileNames
        if(oldFileNames.isNotEmpty()){
            val removeFiles =
                oldFileNames.filter { fileName->
                    uploadFileNames.indexOf(fileName) == -1
            }.toList()
            fileUtil.deleteFiles(removeFiles)
        }


        return mapOf("Success" to "success")
    }

    @DeleteMapping("/{pno}")
    fun remove(
        @PathVariable("pno") pno: Long
    ) : Map<String, String> {

        val oldFileNames = productService.get(pno).uploadFileNames

        productService.remove(pno)

        fileUtil.deleteFiles(oldFileNames)

        return mapOf("RESULT" to "SUCCESS")
    }
}