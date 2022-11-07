package com.group.libraryapp.domain.book

import com.group.libraryapp.dto.book.request.BookStatResponse
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.Optional

interface BookRepository : JpaRepository<Book, Long> {
    fun findByName(bookName: String): Book? //코틀린에서는 Optional이 필요 없다
    /*
    @Query("select new com.group.libraryapp.dto.book.request.BookStatResponse(b.type, COUNT(b.id)) from Book b " +
            "group by b.type")
    fun getStats(): List<BookStatResponse>
     */
}