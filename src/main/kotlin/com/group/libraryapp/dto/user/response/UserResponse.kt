package com.group.libraryapp.dto.user.response

import com.group.libraryapp.domain.user.User

//DTO는 취지에 맞게 data클래스로 만든다
data class UserResponse(
    val id: Long,
    val name: String,
    val age: Int?,
) {
    /*
    constructor(user: User): this(
        id = user.id!!,
        name = user.name,
        age = user.age
    )
     */
    companion object{
        fun of(user: User): UserResponse{
            return UserResponse(
                id = user.id!!,
                name = user.name,
                age = user.age,
            )
        }
    }
}