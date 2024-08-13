package org.zerock.apiserver.config

import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.zerock.apiserver.controller.formatter.LocalDateFormatter

@Configuration
class CustomServletConfig : WebMvcConfigurer {

    override fun addFormatters(registry: FormatterRegistry) {
        registry.addFormatter(LocalDateFormatter())
    }



    // SecurityConfig 설정 하기전에는 여기에 설정하는게 맞음
    // SecurityConfig 설정한 후에는 아래 주석해야함 그리고 CustomSecurityConfig로 이동해야함

//    override fun addCorsMappings(registry: CorsRegistry) {
//        registry.addMapping("/**")
//            .allowedMethods("*")
//            .allowedMethods("HEAD", "GET", "POST", "PUT", "DELETE", "OPTIONS")
//            .maxAge(500)
//            .allowedHeaders("Authorization", "Cache-Control", "Content-Type")
//            //.allowedOrigins("http://localhost:3000")
//        super.addCorsMappings(registry)
//    }


}