package org.zerock.apiserver.util

import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.ZonedDateTime
import java.util.*
import javax.crypto.SecretKey

@Component
class JWTUtil {

    val secretKey = "1234567890123456789012345678901234567890"

    // jwt 1차 토큰 만들고
    fun generateToken(valueMap : Map<String, Any>, min:Long ) : String {

        try {
            val key = Keys.hmacShaKeyFor(secretKey.toByteArray())

            return Jwts.builder()
                .setHeader(mapOf("type" to "JWT"))
                .setClaims(valueMap)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(min).toInstant()))
                .signWith(key)
                .compact()

        } catch (e: Exception) {
            throw RuntimeException(e.message, e)
        }
    }

    // jwt 1차 토큰 검증 하자.
    fun validateToken(token: String) : Map<String, Any> {

        //var claim : Map<String, Object>? = null

        try {
            val key : SecretKey = Keys.hmacShaKeyFor(secretKey.toByteArray())

            val claim = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body /* 실수 주의 parseClaimsJwts가 아님 */
            return claim
        } catch (e: Exception) {
            when(e) {
                is SecurityException -> {
                    throw CustomJWTException("SecurityException")
                }  // Invalid JWT Token
                is MalformedJwtException -> { // --
                    throw CustomJWTException("MalFormed")
                }  // Invalid JWT Token
                is ExpiredJwtException -> { //--
                    throw CustomJWTException("ExpiredJwt")
                }    // Expired JWT Token
                is JwtException -> { //--
                    throw CustomJWTException("JWTError");
                }
                is IllegalArgumentException -> {
                    throw CustomJWTException("MalFormed")
                }   // JWT claims string is empty
                else -> { //--
                    throw CustomJWTException("Error");
                }
            }
        }
    }
}