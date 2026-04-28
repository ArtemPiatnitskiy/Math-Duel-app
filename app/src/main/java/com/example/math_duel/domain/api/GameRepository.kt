package com.example.math_duel.domain.api

import com.example.math_duel.domain.models.Question
import com.example.math_duel.domain.models.Difficulty

interface GameRepository {
    suspend fun generateQuestions(count: Int, difficulty: Difficulty): List<Question>
    suspend fun getRandomQuestion(difficulty: Difficulty): Question
}