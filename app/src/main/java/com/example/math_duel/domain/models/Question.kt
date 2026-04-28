package com.example.math_duel.domain.models

/**
 * Модель математического вопроса
 * @param id уникальный id вопроса
 * @param firstNumber первое число
 * @param secondNumber второе число
 * @param operation операция (+, -, *, /)
 * @param correctAnswer правильный ответ
 * @param answerOptions все варианты ответов (перемешанные)
 */
data class Question(
    val id: Int,
    val firstNumber: Int,
    val secondNumber: Int,
    val operation: String,
    val correctAnswer: Int,
    val answerOptions: List<Int>
)