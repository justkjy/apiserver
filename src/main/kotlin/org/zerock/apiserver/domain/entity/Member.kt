package org.zerock.apiserver.domain.entity

import jakarta.persistence.*

@Entity
data class Member(

    @Id
    var email: String? = null,

    var pw: String = "",

    var nickname: String = "",

    var social: Boolean = false, // 소셜 회원으로 가입된 사람을 구분하기 위한 코드

    // member_member_Role_List로 테이블 생성
    @ElementCollection(fetch = FetchType.LAZY)
    var memberRoleList: MutableSet<MemberRole> = mutableSetOf()

) {

    fun addRole(memberRole : MemberRole) {
        memberRoleList.add(memberRole)
    }

    fun clearRole() {
        memberRoleList.clear()
    }

    fun changeNickname(nickname: String) {
        this.nickname = nickname
    }

    fun changePw(pw: String) {
        this.pw = pw
    }

    fun changeSocial(social: Boolean) {
        this.social = social
    }


}
