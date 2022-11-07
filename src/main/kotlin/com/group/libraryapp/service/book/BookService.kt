package com.group.libraryapp.service.book

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookRepository
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import com.group.libraryapp.dto.book.request.BookLoanRequest
import com.group.libraryapp.dto.book.request.BookRequest
import com.group.libraryapp.dto.book.request.BookReturnRequest
import com.group.libraryapp.dto.book.request.BookStatResponse
import com.group.libraryapp.util.fail
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BookService(
    private val bookRepository: BookRepository,
    private val userRepository: UserRepository,
    private val userLoanHistoryRepository: UserLoanHistoryRepository,
) {
    @Transactional
    fun saveBook(request: BookRequest) {
        val book = Book(request.name, request.type)
        bookRepository.save(book)
    }

    @Transactional
    fun loanBook(request: BookLoanRequest) {
        val book =
            bookRepository.findByName(request.bookName) ?: fail() //항상 반복되므로 리펙토링 가능
        if (userLoanHistoryRepository.findByBookNameAndStatus(
                request.bookName,
                UserLoanStatus.LOANED
            ) != null
        ) {
            throw IllegalArgumentException("진작 대출되어 있는 책입니다")
        }
        val user =
            userRepository.findByName(request.userName) ?: fail()
        user.loanBook(book)
    }

    @Transactional
    fun returnBook(request: BookReturnRequest) {
        val user =
            userRepository.findByName(request.userName) ?: fail()
        user.returnBook(request.bookName)
    }

    @Transactional(readOnly = true)
    fun countLoanedBook(): Int {
        //return userLoanHistoryRepository.findByStatus(UserLoanStatus.LOANED).size //DB에 있는 정보를 모두 가지고와 애플리케이션에서 size를 계산한다
        //기능은 같지만 내부 동작이 아예 다르다.
        //1) 전체 데이터 쿼리 메모리 로딩 + size 2) count 쿼리 타입 변경
        //2번 기능이 더 좋다고 생각한다 -> 데이터가 많아질수록..  OOM이 생길수 있다
        return userLoanHistoryRepository.countByStatus(UserLoanStatus.LOANED).toInt() //숫자만 메모리에 올려 타입만 변경해준다
    }

    @Transactional(readOnly = true)
    fun getBookStatistics(): List<BookStatResponse> {
        /*
        val results = mutableListOf<BookStatResponse>() //MutableList를 쓰면 별로 좋지 않다
        val books = bookRepository.findAll()
        for (book in books) {
            /*
            val targetDto = results.firstOrNull { dto -> book.type == dto.type }
            if (targetDto == null) {
                results.add(BookStatResponse(book.type, 1))
            }else{
                targetDto.plusOne()
            }
             */
            results.firstOrNull { dto -> book.type == dto.type }?.plusOne()
                ?: results.add(BookStatResponse(book.type, 1))
        }
        return results
         */
        /*
        return bookRepository.findAll()//List<Book>
            .groupBy { it -> it.type } //Map<BookType, List<Book>>
            .map { (type, books) -> BookStatResponse(type, books.size) } //List<BookStatResponse>
         */
        return bookRepository.getStats()
    }
}