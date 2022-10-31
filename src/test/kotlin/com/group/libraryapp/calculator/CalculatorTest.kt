package com.group.libraryapp.calculator

class CalculatorTest {
    /**
     * 덧셈 테스트
     */
    fun addTest() {
        //given : 테스트 준비
        val calculator = Calculator(5)

        //when : 테스트 기능 호출
        calculator.add(3)
        //비교 방법1: data클래스 사용
        /*
        val expectedCalculator = Calculator(8)
        if (calculator != expectedCalculator) {
            throw IllegalStateException()
        }
         */
        //비교 방법2: private 키워드를 제거
        //단 이방법은 number 값을 setter를 통해 변경할 수 있기 때문에

        //then : 테스트 결
        if (calculator.number != 8) {
            throw IllegalStateException()
        }
    }


    fun minusTest() {
        //given
        val calculator = Calculator(5)

        //when
        calculator.minus(3)

        //then
        if (calculator.number != 2) {
            throw IllegalStateException()
        }
    }

    fun multiplyTest() {
        //given
        val calculator = Calculator(5)

        //when
        calculator.multiply(3)

        //then
        if (calculator.number != 15) {
            throw IllegalStateException()
        }
    }

    fun divideTest() {
        //given
        val calculator = Calculator(5)

        //when
        calculator.divide(2)

        //then
        if (calculator.number != 2) {
            throw IllegalStateException()
        }
    }

    fun divideExceptionTest() {
        //given
        val calculator = Calculator(5)

        //when : IllegalArgumentException이 발생하기를 기대한다
        try {
            calculator.divide(0)
        }catch (e: IllegalArgumentException){
            if(e.message != "0으로 나눌 수 없습니다.")
                throw IllegalStateException("메시지가 다릅니다.")
            //테스트 성공
            return
        }catch (e: Exception){
            throw IllegalStateException()
        }
        throw IllegalStateException("기대하는 예외가 발생하지 않았습니다.")
    }

}

fun main() {
    val calculatorTest = CalculatorTest()
    calculatorTest.addTest()
    calculatorTest.minusTest()
    calculatorTest.multiplyTest()
    calculatorTest.divideTest()
    calculatorTest.divideExceptionTest()
}