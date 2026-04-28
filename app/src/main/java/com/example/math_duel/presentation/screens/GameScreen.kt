package com.example.math_duel.presentation.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.math_duel.R
import com.example.math_duel.domain.models.GameState
import com.example.math_duel.domain.models.Player

@Composable
fun GameScreen(
    gameState: GameState,
    onAnswerSelected: (playerId: Int, answer: Int) -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    val locked = isLoading || gameState.isRoundLocked

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        PlayerPanel(
            player = gameState.player1,
            playerId = 1,
            answerOptions = gameState.currentQuestion.answerOptions,
            correctAnswer = gameState.currentQuestion.correctAnswer,
            questionId = gameState.currentQuestion.id,
            isRoundLocked = locked,
            enabled = !locked,
            onAnswerSelected = onAnswerSelected,
            modifier = Modifier
                .weight(1f)
                .graphicsLayer(rotationZ = 180f)
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .graphicsLayer(rotationZ = -90f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GameHeader(gameState = gameState)
                Spacer(modifier = Modifier.height(16.dp))
                QuestionCard(gameState = gameState)

                if (isLoading) {
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
            }
        }

        PlayerPanel(
            player = gameState.player2,
            playerId = 2,
            answerOptions = gameState.currentQuestion.answerOptions,
            correctAnswer = gameState.currentQuestion.correctAnswer,
            questionId = gameState.currentQuestion.id,
            isRoundLocked = locked,
            enabled = !locked,
            onAnswerSelected = onAnswerSelected,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun GameHeader(gameState: GameState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(gameState.player1.name, fontWeight = FontWeight.Bold)
        Text(
            text = stringResource(R.string.game_round_format, gameState.questionIndex, gameState.totalQuestions),
            fontWeight = FontWeight.Bold
        )
        Text(gameState.player2.name, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun QuestionCard(gameState: GameState) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = MaterialTheme.shapes.large,
        shadowElevation = 6.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.game_answer_prompt),
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            AnimatedContent(
                targetState = gameState.currentQuestion,
                transitionSpec = {
                    (slideInVertically(animationSpec = tween(400)) { height -> height } + fadeIn(animationSpec = tween(400))).togetherWith(
                        slideOutVertically(animationSpec = tween(400)) { height -> -height } + fadeOut(animationSpec = tween(400))
                    )
                },
                label = "questionAnimation"
            ) { targetQuestion ->
                Text(
                    text = stringResource(
                        R.string.game_question_format,
                        targetQuestion.firstNumber,
                        targetQuestion.operation,
                        targetQuestion.secondNumber
                    ),
                    fontSize = 38.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun PlayerPanel(
    player: Player,
    playerId: Int,
    answerOptions: List<Int>,
    correctAnswer: Int,
    questionId: Int,
    isRoundLocked: Boolean,
    enabled: Boolean,
    onAnswerSelected: (playerId: Int, answer: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedAnswer by remember(questionId) { mutableStateOf<Int?>(null) }
    var showCorrectHighlight by remember(questionId) { mutableStateOf(false) }
    val localEnabled = enabled && !showCorrectHighlight

    Card(
        modifier = modifier.padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = player.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = stringResource(R.string.score_value_format, player.score),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            AnimatedContent(
                targetState = answerOptions,
                transitionSpec = {
                    (fadeIn(animationSpec = tween(500))).togetherWith(fadeOut(animationSpec = tween(300)))
                },
                label = "answersAnimation"
            ) { targetOptions ->
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(targetOptions.size) { index ->
                        AnswerButton(
                            answer = targetOptions[index],
                            correctAnswer = correctAnswer,
                            selectedAnswer = selectedAnswer,
                            isRoundLocked = isRoundLocked,
                            enabled = localEnabled,
                            onClick = {
                                selectedAnswer = targetOptions[index]
                                showCorrectHighlight = true
                            }
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(showCorrectHighlight) {
        if (showCorrectHighlight) {
            delay(500)
            val answer = selectedAnswer
            if (answer != null) onAnswerSelected(playerId, answer)
            showCorrectHighlight = false
        }
    }
}

@Composable
fun AnswerButton(
    answer: Int,
    correctAnswer: Int,
    selectedAnswer: Int?,
    isRoundLocked: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val isCorrect = (selectedAnswer == answer && answer == correctAnswer) || (isRoundLocked && answer == correctAnswer)
    val isWrongSelected = selectedAnswer == answer && answer != correctAnswer

    val colors = when {
        isCorrect -> ButtonDefaults.buttonColors(
            containerColor = Color(0xFF4CAF50),
            contentColor = Color.White,
            disabledContainerColor = Color(0xFF4CAF50),
            disabledContentColor = Color.White
        )
        isWrongSelected -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.error,
            contentColor = MaterialTheme.colorScheme.onError,
            disabledContainerColor = MaterialTheme.colorScheme.error,
            disabledContentColor = MaterialTheme.colorScheme.onError
        )
        else -> ButtonDefaults.buttonColors()
    }

    Button(
        onClick = onClick,
        enabled = enabled,
        colors = colors,
        modifier = Modifier
            .fillMaxWidth()
            .height(68.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Text(
            text = answer.toString(),
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}
