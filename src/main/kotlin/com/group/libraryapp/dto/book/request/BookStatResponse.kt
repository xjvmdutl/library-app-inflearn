package com.group.libraryapp.dto.book.request

import com.group.libraryapp.domain.book.BookType

data class BookStatResponse(
    val type: BookType,
    //var count: Int,
    val count: Long,
) {
    /*
    fun plusOne() {
        count++
    }
     */
}