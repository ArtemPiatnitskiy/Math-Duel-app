package com.example.math_duel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.math_duel.presentation.screens.GameResultScreen
import com.example.math_duel.presentation.screens.GameScreen
import com.example.math_duel.presentation.screens.StartGameScreen
import com.example.math_duel.presentation.viewmodel.GameViewModel
import com.example.math_duel.ui.theme.MathDuelTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    
    private val viewModel: GameViewModel by viewModel()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MathDuelTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GameApp(
                        viewModel = viewModel,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun GameApp(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val gameState by viewModel.gameState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    when {
        gameState == null -> {
            StartGameScreen(
                onStartGame = { player1, player2, difficulty ->
                    viewModel.startNewGame(player1, player2, difficulty)
                },
                modifier = modifier
            )
        }
        gameState?.isGameOver == true -> {
            GameResultScreen(
                gameState = gameState!!,
                onPlayAgain = {
                    viewModel.resetGame()
                },
                modifier = modifier
            )
        }
        gameState != null -> {
            GameScreen(
                gameState = gameState!!,
                onAnswerSelected = { playerId, answer ->
                    viewModel.answerQuestion(playerId, answer)
                },
                isLoading = isLoading,
                modifier = modifier
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameAppPreview() {
    MathDuelTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            StartGameScreen(
                onStartGame = { _, _, _ -> },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        }
    }
}

