package com.example.math_duel.domain.usecases

import com.example.math_duel.domain.models.Question


class CheckAnswerUseCase {
    fun execute(question: Question, userAnswer: Int): Boolean {
        return question.correctAnswer == userAnswer
    }
}

