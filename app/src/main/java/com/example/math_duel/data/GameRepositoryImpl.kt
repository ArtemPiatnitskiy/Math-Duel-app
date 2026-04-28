package com.example.math_duel.data

import com.example.math_duel.domain.api.GameRepository
import com.example.math_duel.domain.models.Question
import kotlin.random.Random

/**
 * Генерация математических вопросов
 */
class GameRepositoryImpl : GameRepository {

    private val operations = listOf("+", "-", "*", "/")
    private var questionId = 0

    override suspend fun generateQuestions(count: Int, difficulty: com.example.math_duel.domain.models.Difficulty): List<Question> {
        return List(count) {
            questionId++
            generateQuestion(questionId, difficulty)
        }
    }

    override suspend fun getRandomQuestion(difficulty: com.example.math_duel.domain.models.Difficulty): Question {
        questionId++
        return generateQuestion(questionId, difficulty)
    }

    private fun generateQuestion(id: Int, difficulty: com.example.math_duel.domain.models.Difficulty): Question {
        val (min, max, ops) = when (difficulty) {
            com.example.math_duel.domain.models.Difficulty.EASY -> Triple(1, 11, listOf("+", "-"))
            com.example.math_duel.domain.models.Difficulty.MEDIUM -> Triple(1, 21, listOf("+", "-", "*"))
            com.example.math_duel.domain.models.Difficulty.HARD -> Triple(1, 51, listOf("+", "-", "*", "/"))
        }

        var firstNumber = Random.nextInt(min, max)  // [min, max-1]
        var secondNumber = Random.nextInt(min, max)
        val operation = ops.random()

        if (operation == "-") {
            if (firstNumber < secondNumber) {
                val temp = firstNumber
                firstNumber = secondNumber
                secondNumber = temp
            }
        } else if (operation == "/") {
            // Ensure divisible
            val multi = Random.nextInt(1, max)
            firstNumber = secondNumber * multi
        }

        val correctAnswer = when (operation) {
            "+" -> firstNumber + secondNumber
            "-" -> firstNumber - secondNumber
            "*" -> firstNumber * secondNumber
            "/" -> {
                firstNumber / secondNumber
            }
            else -> 0
        }

        val answerOptions = generateAnswerOptions(correctAnswer, difficulty)

        return Question(
            id = id,
            firstNumber = firstNumber,
            secondNumber = secondNumber,
            operation = operation,
            correctAnswer = correctAnswer,
            answerOptions = answerOptions
        )
    }


    private fun generateAnswerOptions(correctAnswer: Int, difficulty: com.example.math_duel.domain.models.Difficulty): List<Int> {
        val options = mutableSetOf(correctAnswer)
        val deltaRange = when (difficulty) {
            com.example.math_duel.domain.models.Difficulty.EASY -> -3..3
            com.example.math_duel.domain.models.Difficulty.MEDIUM -> -8..8
            com.example.math_duel.domain.models.Difficulty.HARD -> -20..20
        }

        while (options.size < 4) {
            val randomAnswer = correctAnswer + Random.nextInt(deltaRange.first, deltaRange.last + 1)
            if (randomAnswer != correctAnswer && randomAnswer >= 0 && randomAnswer <= 10000) {
                options.add(randomAnswer)
            }
        }

        return options.shuffled()
    }
}
