package org.zerock.apiserver.domain.dto.page

import kotlinx.serialization.Serializable
import java.util.stream.Collectors
import java.util.stream.IntStream
import kotlin.math.ceil

@Serializable
data class PageResponseDTO<E>(
    var dtoList : List<E>,

    var pageNumList: List<Int>? = null,

    var pageRequestDTO : PageRequestDTO,

    var prev : Boolean = false,

    var next : Boolean = false,

    var totalCount: Int,

    var prevPage: Int = 0,

    var nextPage: Int = 0,

    val totalPage : Int = 0,

    val current : Int  = 0
) {
    fun pageResponseInit(pageRequestDTO: PageRequestDTO) {

        //var end = ((ceil(pageRequestDTO.page.toDouble() / 10.0) * 100.0) * 10).toInt()
        val valueCeil = ceil(pageRequestDTO.page.toDouble() / 10.0)

        var end = (valueCeil  * 10).toInt()

        val start = end - 9

        val last = (ceil((totalCount/pageRequestDTO.size.toDouble()))).toInt()

        if(end > last) {
            end = last
        }
        this.prev = start > 1;

        this.next = totalCount > end * pageRequestDTO.size

        val list  = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList())

        pageNumList = list

        if (prev) {
            this.prevPage = start - 1
        }

        if (next) {
            this.nextPage = end + 1
        }
    }
}
