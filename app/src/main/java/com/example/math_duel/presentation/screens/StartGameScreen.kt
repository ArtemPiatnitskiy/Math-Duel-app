package com.example.math_duel.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.graphics.Color
import com.example.math_duel.domain.models.Difficulty
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.math_duel.R

@Composable
fun StartGameScreen(
    onStartGame: (player1Name: String, player2Name: String, difficulty: Difficulty) -> Unit,
    modifier: Modifier = Modifier
) {
    var player1Name by remember { mutableStateOf("") }
    var player2Name by remember { mutableStateOf("") }
    var difficulty by remember { mutableStateOf(Difficulty.MEDIUM) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.start_title),
            fontSize = 36.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Text(
            text = stringResource(R.string.start_subtitle),
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = player1Name,
            onValueChange = { player1Name = it },
            label = { Text(stringResource(R.string.player1_name_label)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        )

        OutlinedTextField(
            value = player2Name,
            onValueChange = { player2Name = it },
            label = { Text(stringResource(R.string.player2_name_label)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = stringResource(R.string.difficulty_label), modifier = Modifier.padding(bottom = 8.dp))
        Row(modifier = Modifier.padding(bottom = 12.dp)) {
            Difficulty.entries.forEach { d ->
                val selected = d == difficulty
                Button(
                    onClick = { difficulty = d },
                    colors = if (selected) ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50), contentColor = Color.White) else ButtonDefaults.buttonColors(),
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(text = d.name.lowercase().replaceFirstChar { it.uppercase() })
                }
            }
        }

        Button(
            onClick = {
                onStartGame(player1Name.trim(), player2Name.trim(), difficulty)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            enabled = player1Name.isNotBlank() && player2Name.isNotBlank()
        ) {
            Text(stringResource(R.string.start_game_button), fontSize = 18.sp)
        }
    }
}

