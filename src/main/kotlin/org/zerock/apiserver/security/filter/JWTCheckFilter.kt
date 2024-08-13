package org.zerock.apiserver.security.filter

import com.google.gson.Gson
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.aspectj.weaver.tools.cache.SimpleCacheFactory.path
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import org.zerock.apiserver.domain.dto.MemberDTO
import org.zerock.apiserver.logger
import org.zerock.apiserver.util.JWTUtil
import java.util.Objects

// api가 들어왔을때 필터를 확인한다.
class JWTCheckFilter(
    private val jwtUtil: JWTUtil
) : OncePerRequestFilter() {


    // jwt 3차 필터 예외 처리 등록
    // Login 같은 필터를 적용하면 안되는 api 가 들어 왔을때 아래 함수가 필요
    // login은 토큰이 없기 때문에 필터 적용하면 안된다.
    override fun shouldNotFilter(request: HttpServletRequest): Boolean {

        val path = request.requestURI
        logger().info("check uri -----------------$path")

        // 필터 체크 하지 않는다.
        if(path.startsWith("/api/member/")) {
            return true
        }

        //return super.shouldNotFilter(request)
        return false // false => 체크를 해야한다.
                     // true -> 체크 하지 말고 보내자.
    }

    // jwt 5차 필터 인증 확인
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        logger().info("----------------------------------------")
        logger().info("----------------------------------------")
        logger().info("----------------------------------------")
        //SecurityFilterChain 에 등록된 api 주소 말고 다른 api 주소로 테스트 해서 위 로그가 찍히는지 확인하자.
        // 그리고 아래 필터 가져 오기 코드를 작성하여 Bearer 필터넣어서 체크 하자.

        // 필터 가져 오기
        // 참조 사이트 https://developer.mozilla.org/ko/docs/Web/HTTP/Headers/Authorization
        // 문법 확인 : Authorization: <type> <credentials>
        // types는 "bearer"를 사용한다.
        //         Head를 가져오면 bearer+ " " 뒤만 가져와야 하기때문에 앞의 7자리 짤라낸다.
        try {
            val authHeader = request.getHeader("Authorization")
            val accessToken : String = authHeader!!.substringAfter("Bearer ") // bearer뒤에 공백 있음
            logger().info("accessToken: $accessToken")
            val claims : Map<String, Any> = jwtUtil.validateToken(accessToken) // 문제 생기면 예외 처리
            logger().info("claims: $claims")

            // 필터가 들어오면 어디로 가야할지 목적지 정함
            // jwt 6차-3 이전은 아래 주석 제거
            //filterChain.doFilter(request, response)

            // jwt 6차-3 추가
            // spring security context 를 생성한다.
            // 토큰이 성공했다면 토큰으로 이루어진 사용자에 대한 정보를 가져 올 수가 있음
            val email: String = claims["email"].toString()
            val pw : String = claims["pw"].toString()
            val nickname : String = claims["nickname"].toString()
            val social : Boolean = claims["social"] as Boolean

            @Suppress("UNCHECKED_CAST")
            val roleName: List<String> = claims["roleNames"] as? List<String> ?: listOf()

            val memberDTO = MemberDTO(email, pw, nickname, social, roleName)

            logger.info("--------------------------------------------")
            logger.info("$memberDTO")
            logger.info("${memberDTO.authorities}")

            val authenticationToken : UsernamePasswordAuthenticationToken
            = UsernamePasswordAuthenticationToken(memberDTO, pw, memberDTO.authorities)


            SecurityContextHolder.getContext().authentication = authenticationToken

            filterChain.doFilter(request, response)
            // jwt 6차-3 추가 끝
        } catch(e : Exception) { // Access Token에 문제가 생겼을때 발생하는 예외 처리
            logger.error("JWT Check Error............")
            logger().error(e.message)

            val gson = Gson()
            val msg = gson.toJson(mapOf("error" to "ERROR_ACCESS_TOKEN"))

            response.contentType = "application/json; charset=utf-8"
            val printWriter = response.writer
            printWriter.println(gson.toJson(msg))
            printWriter.close()
        }
    }
}