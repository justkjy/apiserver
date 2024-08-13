package org.zerock.apiserver.domain.dto

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import java.util.stream.Collectors

data class MemberDTO (
    val email : String,
    val pw : String,
    val nickname : String,
    val social : Boolean,
    val roleNames: List<String>
) : User(
        email,
        pw,
        roleNames.stream().map {
            str -> SimpleGrantedAuthority("ROLE_$str")
        }.collect(Collectors.toList()
    )
) {

    fun getClaims() : Map<String, Any> {
        return mapOf(
            "email" to email,
            "pw" to pw,
            "nickname" to nickname,
            "social" to social,
            "roleNames" to roleNames
        )
    }
}