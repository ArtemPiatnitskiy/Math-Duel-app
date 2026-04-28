package com.example.math_duel.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.math_duel.domain.models.GameState
import com.example.math_duel.domain.models.Player
import com.example.math_duel.domain.usecases.CheckAnswerUseCase
import com.example.math_duel.domain.usecases.GetQuestionUseCase
import com.example.math_duel.domain.models.Difficulty
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class GameViewModel(
    private val getQuestionUseCase: GetQuestionUseCase,
    private val checkAnswerUseCase: CheckAnswerUseCase
) : ViewModel() {

    private val answerMutex = Mutex()
    private val _gameState = MutableStateFlow<GameState?>(null)
    val gameState: StateFlow<GameState?> = _gameState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var currentDifficulty: Difficulty = Difficulty.MEDIUM

    fun startNewGame(player1Name: String, player2Name: String, difficulty: Difficulty = Difficulty.MEDIUM) {
        currentDifficulty = difficulty
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val firstQuestion = getQuestionUseCase.execute(difficulty)

                val newGameState = GameState(
                    player1 = Player(id = 1, name = player1Name, score = 0),
                    player2 = Player(id = 2, name = player2Name, score = 0),
                    currentQuestion = firstQuestion,
                    questionIndex = 1,
                    totalQuestions = 10,
                    isGameOver = false
                )

                _gameState.value = newGameState
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun answerQuestion(playerId: Int, userAnswer: Int) {
        viewModelScope.launch {
            answerMutex.withLock {
                val currentState = _gameState.value ?: return@withLock
                if (currentState.isGameOver || currentState.isRoundLocked) return@withLock
                if (!checkAnswerUseCase.execute(currentState.currentQuestion, userAnswer)) return@withLock

                val updatedPlayer1 = if (playerId == 1) {
                    currentState.player1.copy(score = currentState.player1.score + 1)
                } else {
                    currentState.player1
                }
                val updatedPlayer2 = if (playerId == 2) {
                    currentState.player2.copy(score = currentState.player2.score + 1)
                } else {
                    currentState.player2
                }

                val lockedState = currentState.copy(
                    player1 = updatedPlayer1,
                    player2 = updatedPlayer2,
                    isRoundLocked = true,
                    lastCorrectPlayerId = playerId
                )
                _gameState.value = lockedState

                val nextQuestionIndex = currentState.questionIndex + 1
                if (nextQuestionIndex > currentState.totalQuestions) {
                    val winner = when {
                        updatedPlayer1.score > updatedPlayer2.score -> updatedPlayer1
                        updatedPlayer2.score > updatedPlayer1.score -> updatedPlayer2
                        else -> null
                    }

                    _gameState.value = lockedState.copy(
                        isRoundLocked = false,
                        isGameOver = true,
                        winner = winner
                    )
                    return@withLock
                }

                _isLoading.value = true
                try {
                    val nextQuestion = getQuestionUseCase.execute(currentDifficulty)
                    _gameState.value = lockedState.copy(
                        currentQuestion = nextQuestion,
                        questionIndex = nextQuestionIndex,
                        isRoundLocked = false,
                        lastCorrectPlayerId = null
                    )
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }

    fun resetGame() {
        _gameState.value = null
    }
}
