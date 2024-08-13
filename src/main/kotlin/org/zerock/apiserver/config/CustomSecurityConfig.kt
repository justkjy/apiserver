package org.zerock.apiserver.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.zerock.apiserver.logger
import org.zerock.apiserver.security.filter.JWTCheckFilter
import org.zerock.apiserver.security.handler.APILoginFailHandler
import org.zerock.apiserver.security.handler.APILoginSuccessHandler
import org.zerock.apiserver.security.handler.CustomAccessDeniedHandler
import org.zerock.apiserver.util.JWTUtil

@Configuration
// jwt 6차 -- 1 @EnableMethodSecurity 추가 -> 룰 적용 절차... 접근 권한 설정
@EnableMethodSecurity
class CustomSecurityConfig(
    private val jwtUtil: JWTUtil,
    private val customAccessDeniedHandler: CustomAccessDeniedHandler
) {

    // 보안 로그인 설정
    @Bean
    @Throws(Exception::class)
    fun SecurityFilterChain(http : HttpSecurity) : SecurityFilterChain {
        logger().info("------------------------security config----------------")

        return http
            .cors{}
            .sessionManagement { sessionConfig ->
                sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .csrf {config-> config.disable()}
            .formLogin { config->
                config.loginPage("/api/member/login")
                // 수정
                config.successHandler(APILoginSuccessHandler(jwtUtil))
                config.failureHandler(APILoginFailHandler())
            }
            // jwt 4차 필터 적용 클레스 작성
            .addFilterBefore(
                JWTCheckFilter(jwtUtil), UsernamePasswordAuthenticationFilter::class.java
            ) // UsernamePasswordAuthenticationFilter가 동작하기 전에 JWTCheckFilter를 실행
            .exceptionHandling{ config ->
                    config.accessDeniedHandler(customAccessDeniedHandler)
            }
            .build()
    }

    // 사용자 계정 암호화 할때 사용
    @Bean
    fun passWordEncoder() : PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun corsConfigurationSource() : CorsConfigurationSource {
        val configuration = CorsConfiguration()

        // 허용할 URL들
        //configuration.allowedOrigins = listOf("http://localhost:3000")
        configuration.allowedOrigins = listOf("*")
        // 허용할 Method들
        configuration.allowedMethods = listOf("HEAD", "POST", "GET", "DELETE", "PUT", "OPTIONS")
        // 허용할 Header들
        //configuration.allowedHeaders = listOf("*")
        configuration.allowedHeaders = listOf("Authorization", "Cache-Control", "Content-Type")
        // 세션 쿠키를 유지할 것 인지
        configuration.allowCredentials = true

        // cors 적용
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

}