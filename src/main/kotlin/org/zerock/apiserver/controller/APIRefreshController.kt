package org.zerock.apiserver.controller

import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.zerock.apiserver.logger
import org.zerock.apiserver.util.CustomJWTException
import org.zerock.apiserver.util.JWTUtil
import java.util.*

// jwt 8차-1 토큰 재 발행
@RestController
class APIRefreshController(
    private val jwtUtil: JWTUtil
) {

    @RequestMapping("/api/member/refresh")
    fun refresh(
        @RequestHeader("Authorization") authHeader: String?,
        refreshToken : String?
    ) : Map<String, Any>  {
        if(refreshToken == null){
            throw CustomJWTException("NULL_REFRESH_TOKEN")
        }

        if(authHeader == null || authHeader.length < 7){
            throw CustomJWTException("INVALID STRING")
        }

        //Bearer xxx
        val accessToken = authHeader.substring(7)

        // AccessToken 만료 여부 확인
        if(!checkExpiredToken(accessToken)) {
            return mapOf("accessToken" to accessToken, "refreshToken" to refreshToken)
        }

        val claims = jwtUtil.validateToken(refreshToken)

        logger().info("refresh... claims: $claims")

        val newAccessToken = jwtUtil.generateToken(claims, 10)

        val newRefreshToken = if(checkTime( claims["exp"] as Int)) {
            jwtUtil.generateToken(claims, 60*24)
        } else {
            refreshToken
        }

        return mapOf("accessToken" to newAccessToken, "refreshToken" to newRefreshToken)
    }

    private fun checkTime(exp: Int) : Boolean {
        //Convert to Basic Latin
        val expDate = Date(exp.toLong() * 1000)

        // 현재 시간과의 차이 계산 - 밀리 세컨즈
        val gap: Long = expDate.time - System.currentTimeMillis()

        // 분단위 계산
        val leftMin: Long = gap / (1000 * 60)

        // 1시간도 안남았는지
        return leftMin < 60
    }

    private fun checkExpiredToken(token: String): Boolean{
        try {
            jwtUtil.validateToken(token)
        } catch (ex: CustomJWTException) {
            if(ex.message!!.contains("Expired")){
                return true
            }
        }
        return false
    }
}