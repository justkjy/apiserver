package org.zerock.apiserver.repository

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.zerock.apiserver.domain.entity.Member

interface MemberRepository : JpaRepository<Member, Long> {
    @Override
    fun findByEmailIgnoreCase(@Param("email") email: String): Member?

    @EntityGraph(attributePaths = ["memberRoleList"]) // memberRoleList 정보도 가져 와라
    @Query("SELECT m FROM Member m WHERE m.email=:email")
    fun getWithRoles(@Param("email") email: String ): Member
}