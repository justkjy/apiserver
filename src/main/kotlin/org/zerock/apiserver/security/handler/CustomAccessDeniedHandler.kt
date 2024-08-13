package org.zerock.apiserver.security.handler

import com.google.gson.Gson
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

// jwt 7차 -- 1
@Component
class CustomAccessDeniedHandler : AccessDeniedHandler {

    override fun handle(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        accessDeniedException: AccessDeniedException?
    ) {
        val gson : Gson = Gson()

        val jsonStr = gson.toJson(mapOf("error" to "ERROR_ACCESSIONED"))

        response?.contentType = "application/json"
        response?.status = HttpStatus.FORBIDDEN.value()
        val printWriter = response?.writer
        printWriter?.println(jsonStr)
        printWriter?.close()

    }
}