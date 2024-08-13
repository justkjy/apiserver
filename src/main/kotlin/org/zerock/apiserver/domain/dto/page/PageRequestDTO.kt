package org.zerock.apiserver.domain.dto.page

data class PageRequestDTO(
    // 현재 페이지
    var page: Int = 1,

    // 한페이지당 보여줄 페이지
    var size: Int = 10,
)