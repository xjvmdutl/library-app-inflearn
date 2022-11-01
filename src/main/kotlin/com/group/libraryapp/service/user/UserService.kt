package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.dto.user.request.UserCreateRequest
import com.group.libraryapp.dto.user.request.UserUpdateRequest
import com.group.libraryapp.dto.user.response.UserResponse
import com.group.libraryapp.util.fail
import com.group.libraryapp.util.findByIdOrThrow
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
) {

    //Transactional 기능을 사용하기 위해서는 오버라이드 될 수 있어야 하는데, 코틀린은 함수를 기본적으로 상속이 불가능하기 떄문에 오류가 발생한다.
    //open 키워드를 붙여서 상속이 가능하도록 해도 되지만, 플러그인을 추가해도 된다(spring)
    @Transactional
    fun saveUser(request: UserCreateRequest) {
        val newUser = User(request.name, request.age)
        userRepository.save(newUser)
    }

    @Transactional(readOnly = true)
    fun getUsers(): List<UserResponse> {
        //return userRepository.findAll().map { user -> UserResponse(user) }
        //return userRepository.findAll().map { UserResponse(it) }
        return userRepository.findAll().map { UserResponse.of(it) }
    }

    @Transactional
    fun updateUserName(request: UserUpdateRequest) {
        //val user = userRepository.findByIdOrNull(request.id) ?: fail() //JPA에서 findById()를 Optional로 주기 떄문에 제어할 수 없을거 같지만, 확장함수를 사용하면 가능하다
        val user = userRepository.findByIdOrThrow(request.id)
        user.updateName(request.name)
    }

    @Transactional
    fun deleteUser(name: String) {
        //val user = userRepository.findByName(name) ?: throw IllegalArgumentException() //엘비스 연산자 사용
        val user = userRepository.findByName(name) ?: fail()
        userRepository.delete(user)
    }
}