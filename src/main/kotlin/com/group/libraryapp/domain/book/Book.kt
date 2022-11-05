package com.group.libraryapp.domain.book

import javax.persistence.*

@Entity
class Book(
    val name: String,
    @Enumerated(EnumType.STRING)
    val type: BookType, //책 타입 추가
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
) {
    //기본 생성자가 없는 오류가 발생(플러그인을 받으면 오류 해결)

    init { // 초기화 블록
        if (name.isBlank()) {
            throw IllegalArgumentException("이름은 비어 있을 수 없습니다")
        }
    }

    companion object{ //동반객체가 가장 아래 들어가는게 컨벤션
        //Object Mother 패턴
        fun fixture(
            name: String="책 이름",
            type: BookType = BookType.COMPUTER,
            id:Long? = null,
        ): Book{
            return Book(
                name = name,
                type = type,
                id = id,
            )
        }
    }
}