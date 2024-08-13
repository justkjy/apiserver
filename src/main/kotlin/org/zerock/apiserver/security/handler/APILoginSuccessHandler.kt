package org.zerock.apiserver.security.handler

import com.google.gson.Gson
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.zerock.apiserver.domain.dto.MemberDTO
import org.zerock.apiserver.logger
import org.zerock.apiserver.util.JWTUtil

// password와 id 맞으면 아래 클레스 실행
// CustomSecurityConfig > SecurityFilterChain > config.successHandler(APILoginSuccessHandler(jwtUtil)) 에 지정
class APILoginSuccessHandler(
    private val jwtUtil: JWTUtil
) : AuthenticationSuccessHandler {
    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?
    ) {
        logger().info("-----------------------")
        logger().info("Authentication success")
        logger().info("-----------------------")

        val memberDTO  : MemberDTO = authentication?.principal as MemberDTO
        // 실제는 암호를 가리자...

        val claims : MutableMap<String, Any> = memberDTO.getClaims().toMutableMap()

        // jwt 2차
//        claims["accessToken"] = ""
//        claims["refreshToken"] = ""
        // 토큰 유효 기간 : 10분
        // 리플레쉬 토큰 : 24시간
        val accessToken = jwtUtil.generateToken(claims, 5)
        val refreshToken = jwtUtil.generateToken(claims, 60*24)
        claims["accessToken"] = accessToken
        claims["refreshToken"] = refreshToken

        val gson = Gson()

        val jsonStr = gson.toJson(claims)

        response?.contentType = "application/json;charset=UTF-8"

        val printWriter = response?.writer
        printWriter?.println(jsonStr)
        // jwt 문자열이 만들어짐 -> 클라이언트(react)로 전달
        // 클라이언트(react)에서 보내는건 오직 JWT 문자열만 보낸다.
        // JWT 문자열 안에 있는 페이로드를 분석해서 처리해야된다.
        // 이 처리는 필터를 쓴다. > JWTCheckFilter 클레스 확인
        logger().info("-------claims to json : $jsonStr----------------")
        printWriter?.close()
    }
}