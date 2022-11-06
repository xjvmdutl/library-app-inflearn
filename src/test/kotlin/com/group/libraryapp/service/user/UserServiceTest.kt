package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import com.group.libraryapp.dto.user.request.UserCreateRequest
import com.group.libraryapp.dto.user.request.UserUpdateRequest
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest //스프링 부트 테스트(빈으로 등록된 객체를 테스트해야되므로)
class UserServiceTest @Autowired constructor(
    /*
    @Autowired private val userRepository: UserRepository,
    @Autowired private val userService: UserService,
     */
    private val userRepository: UserRepository,
    private val userService: UserService,
    private val userLoanHistoryRepository: UserLoanHistoryRepository,
) {

    @AfterEach
    fun clear() {
        println("CLEAN 시작")
        userRepository.deleteAll()
    }


    @Test
    @DisplayName("유저 저장이 정상 동작한다.")
    fun saveUserTest() {
        //given
        val request = UserCreateRequest("최태현", null)

        //when
        userService.saveUser(request)

        //then
        //실제 DB에 값이 있는지 확인
        val results = userRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results[0].name).isEqualTo("최태현")
        assertThat(results[0].age).isNull() //코틀린은 age가 null일지 아닐지를 모르기 때문에 null이 아니라고 단언한다(코틀린 플렛폼)
        //null을 허용하기 위해서는 @nullable 어노테이션을 붙여야 한다.
    }

    @Test
    @DisplayName("유저 조회가 정상 동작한다.")
    fun getUsersTest() {
        //given
        userRepository.saveAll(
            listOf(
                User("A", 20),
                User("B", null)
            )
        )

        //when
        val results = userService.getUsers()

        //then
        //단위 실행하면 오류가 발생하지 않지만, 모두 실행하면 SpringContext를 공유하기 떄문에 문제가 된다.
        //이를 해결하기 위해서는 @AfterEach에서 database를 초기화 해주자
        assertThat(results).hasSize(2)
        assertThat(results).extracting("name").containsExactlyInAnyOrder("A", "B") //["A", "B"]
        assertThat(results).extracting("age").containsExactlyInAnyOrder(20, null) //[20, null]
    }

    @Test
    @DisplayName("유저 업데이트가 정상 동작한다.")
    fun updateUserNameTest() {
        //given
        val saveUser = userRepository.save(
            User(
                "A",
                null
            )
        )
        val request = UserUpdateRequest(saveUser.id!!, "B")

        //when
        userService.updateUserName(request)

        //then
        val result = userRepository.findAll()[0]
        assertThat(result.name).isEqualTo("B")
    }

    @Test
    @DisplayName("유저 삭제가 정상 동작한다.")
    fun deleteUserTest() {
        //given
        userRepository.save(User("A", null))

        //when
        userService.deleteUser("A")

        //then
        assertThat(userRepository.findAll()).isEmpty()
    }

    @Test
    @DisplayName("대출 기록이 없는 유저도 응답에 포함된다.")
    fun getUserLoanHistoriesTest1() {
        //given
        userRepository.save(User("A", null))

        //when
        val result = userService.getUserLoanHistories()

        //then
        assertThat(result).hasSize(1)
        assertThat(result[0].name).isEqualTo("A")
        assertThat(result[0].books).isEmpty()
    }

    @Test
    @DisplayName("대출 기록이 많은 유저의 응답이 정상 동작한다")
    fun getUserLoanHistoriesTest2() {
        //given
        val saveUser = userRepository.save(User("A", null))
        userLoanHistoryRepository.saveAll(
            listOf(
                UserLoanHistory.fixture(saveUser, "책1", UserLoanStatus.LOANED),
                UserLoanHistory.fixture(saveUser, "책2", UserLoanStatus.LOANED),
                UserLoanHistory.fixture(saveUser, "책3", UserLoanStatus.RETURNED),
            )
        )

        //when
        val result = userService.getUserLoanHistories()

        //then
        assertThat(result).hasSize(1)
        assertThat(result[0].name).isEqualTo("A")
        assertThat(result[0].books).hasSize(3)
        assertThat(result[0].books).extracting("name").containsExactlyInAnyOrder("책1", "책2", "책3")
        assertThat(result[0].books).extracting("isReturn")
            .containsExactlyInAnyOrder(false, false, true)
    }

    @Test
    @DisplayName("두 경우가 합쳐진 테스트")
    fun getUserLoanHistoriesTest3() {
        //given
        val saveUsers = userRepository.saveAll(listOf(
            User("A", null),
            User("B", null)))
        userLoanHistoryRepository.saveAll(
            listOf(
                UserLoanHistory.fixture(saveUsers[0], "책1", UserLoanStatus.LOANED),
                UserLoanHistory.fixture(saveUsers[0], "책2", UserLoanStatus.LOANED),
                UserLoanHistory.fixture(saveUsers[0], "책3", UserLoanStatus.RETURNED),
            )
        )

        //when
        val result = userService.getUserLoanHistories()

        //then
        assertThat(result).hasSize(2)
        val userAResult = result.first{ it.name == "A"}

        assertThat(userAResult.name).isEqualTo("A")
        assertThat(userAResult.books).hasSize(3)
        assertThat(userAResult.books).extracting("name").containsExactlyInAnyOrder("책1", "책2", "책3")
        assertThat(userAResult.books).extracting("isReturn")
            .containsExactlyInAnyOrder(false, false, true)

        val userBResult = result.first { it.name == "B" }
        assertThat(userBResult.books).isEmpty()
    }
}