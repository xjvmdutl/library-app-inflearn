package com.group.libraryapp.calculator

class Calculator(
    //private var _number: Int //생성자로 받아 이 값을 계속 업데이트 할 예정
    var number: Int
) {
    /*
    val number: Int
        get() = this.number
     */
    fun add(operand: Int) {
        this.number += operand
    }

    fun minus(operand: Int) {
        this.number -= operand
    }

    fun multiply(operand: Int) {
        this.number *= operand
    }

    fun divide(operand: Int) {
        if (operand == 0)
            throw IllegalArgumentException("0으로 나눌 수 없습니다.")
        this.number /= operand
    }
}