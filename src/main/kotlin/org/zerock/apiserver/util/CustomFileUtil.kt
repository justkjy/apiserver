package org.zerock.apiserver.util

import jakarta.annotation.PostConstruct
import net.coobird.thumbnailator.Thumbnails
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.HandlerExceptionResolver
import org.zerock.apiserver.logger
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import java.util.function.Consumer

@Component
class CustomFileUtil(
    @Value("\${spring.file.upload.path}")
    val propertyValue: String,
    @Qualifier("handlerExceptionResolver") private val handlerExceptionResolver: HandlerExceptionResolver
) {

    lateinit var uploadPath: String

    init{
//        logger().info("File uploadPath")
//
//
//        val tempFolder = File(propertyValue)
//
//        if(!tempFolder.exists()){
//            tempFolder.mkdirs()
//        }
//
//        uploadPath = tempFolder.absolutePath
//
//        logger().info("====================Upload path: $uploadPath========================")

    }

    @PostConstruct
    fun init() {
        logger().info("PostConstruct init")

        val tempFolder = File(propertyValue)

        if(!tempFolder.exists()){
            tempFolder.mkdirs()
        }

        uploadPath = tempFolder.absolutePath

        logger().info("====================Upload path: $uploadPath========================")

    }

    fun  saveFiles(
        files:List<MultipartFile>?
    ):List<String>?  {
        if(files == null) {
            return null
        }

        val uploadNames = mutableListOf<String>()

        for(file: MultipartFile in files){

            val saveName:String = UUID.randomUUID().toString()+"_"+file.originalFilename

            val savePath = Paths.get(uploadPath,saveName)

            try {
                //Files.copy(file.inputStream, savePath, StandardCopyOption.REPLACE_EXISTING) // 원본 파일 업로드 이렇게 쓰면 에러 난다.

                Files.copy(file.inputStream, savePath) // 원본 파일 업로드

                val contentType = file.contentType // 파일 타입 확인

                if(contentType != null && contentType.startsWith("image") ) {
                    val thumbnailPath = Paths.get(uploadPath, "s_$saveName")

                    Thumbnails.of(savePath.toFile())
                        .outputFormat("jpg")
                        .size(200, 200)
                        .toFile(thumbnailPath.toFile());
                }
                uploadNames.add(saveName)
            } catch(e: IOException){
                return null
            }
        }
        return uploadNames
    }

    fun getFile(fileName: String) : ResponseEntity<Resource> {
        val resource: Resource = FileSystemResource(uploadPath + File.separator + fileName).let{
            when(it.isReadable) {
                true -> it
                else -> {
                    // resource가 없을 수가 있을다 체크, 없으면 upload 폴더에 있는 default 파일을 전달
                    FileSystemResource(uploadPath + File.separator + "default.jpg")
                }
            }
        }
        val headers = HttpHeaders()

        try {
            headers.add("content-type", Files.probeContentType(resource.file.toPath()))
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

        return ResponseEntity.ok().headers(headers).body(resource)
    }

    fun deleteFiles(fileNames : List<String>) {


        // 파일명에 문제가 있을때
        if (fileNames.isEmpty()) {
            return
        }

        // 받은 파일이 여러개이니깐
        fileNames.forEach(Consumer { fileName: String ->

            // 썸네일 삭제
            val thumbnailFileName = "s_$fileName"

            val thumbnailPath = Paths.get(uploadPath, thumbnailFileName)
            val filePath = Paths.get(uploadPath, fileName)

            // 파일 처리는 try ~ catch 사용한다. 예외 사항이 많기 때문
            try {
                Files.deleteIfExists(filePath)
                Files.deleteIfExists(thumbnailPath)
            } catch (e: IOException) {
                throw java.lang.RuntimeException(e.message)
            }
        })
    }
}