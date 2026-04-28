package com.example.math_duel.domain.models

data class GameState(
    val player1: Player,
    val player2: Player,
    val currentQuestion: Question,
    val questionIndex: Int,
    val totalQuestions: Int,
    val isRoundLocked: Boolean = false,
    val lastCorrectPlayerId: Int? = null,
    val isGameOver: Boolean = false,
    val winner: Player? = null
)

