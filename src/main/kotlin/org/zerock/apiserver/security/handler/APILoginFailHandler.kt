package org.zerock.apiserver.security.handler

import com.google.gson.Gson
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.zerock.apiserver.logger
import javax.naming.AuthenticationException

class APILoginFailHandler : AuthenticationFailureHandler {

    override fun onAuthenticationFailure(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        exception: org.springframework.security.core.AuthenticationException?
    ) {
        logger().info("-----------------------")
        logger().info("Authentication success" + exception.toString())
        logger().info("-----------------------")

        val gson = Gson()

        val jsonStr = gson.toJson(mapOf("error" to "ERROR_LOGIN"))

        response?.contentType = "application/json; charset=utf-8"
        val printWriter = response?.writer
        printWriter?.println(jsonStr)
        printWriter?.close()
    }
}