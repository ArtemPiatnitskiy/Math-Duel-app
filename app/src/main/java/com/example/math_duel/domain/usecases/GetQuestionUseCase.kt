package com.example.math_duel.domain.usecases

import com.example.math_duel.domain.api.GameRepository
import com.example.math_duel.domain.models.Question
import com.example.math_duel.domain.models.Difficulty

class GetQuestionUseCase(private val repository: GameRepository) {
    suspend fun execute(difficulty: Difficulty = Difficulty.MEDIUM): Question {
        return repository.getRandomQuestion(difficulty)
    }
}



