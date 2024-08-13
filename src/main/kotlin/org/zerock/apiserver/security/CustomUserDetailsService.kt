package org.zerock.apiserver.security

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.zerock.apiserver.domain.dto.MemberDTO
import org.zerock.apiserver.logger
import org.zerock.apiserver.repository.MemberRepository

@Service
class CustomUserDetailsService (
    private val memberRepository: MemberRepository
): UserDetailsService  {

    override fun loadUserByUsername(username: String?): UserDetails {
        logger().info("----------------loadUserByuserName : $username-------------")



        username?.let{
            if(memberRepository.findByEmailIgnoreCase(username) == null) {
                throw UsernameNotFoundException("Username $username not found")
            }

            val member = memberRepository.getWithRoles(username)

            val memberDTO = MemberDTO(
                member.email ?: "",
                member.pw?:"",
                member.nickname?:"",
                member.social?:false,
                member.memberRoleList.map { role -> role.name }
            )
            println(memberDTO)
            return memberDTO

        } ?: kotlin.run {
            throw UsernameNotFoundException("Username $username not found")
        }

    }
}