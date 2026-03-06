package br.edu.ifsp.scl.sc3037291.trucoscoreboardcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrucoScoreBoardApp()
        }
    }
}

@Composable
fun TrucoScoreBoardApp() {
    var scoreA by remember { mutableIntStateOf(0) }
    var scoreB by remember { mutableIntStateOf(0) }
    var dialogMessage by remember { mutableStateOf<String?>(null) }
    var gameOver by remember { mutableStateOf(false) }

    fun addPoints(teamA: Boolean, points: Int) {
        if (gameOver) return

        if (teamA) scoreA = minOf(scoreA + points, 12)
        else scoreB = minOf(scoreB + points, 12)

        when {
            scoreA >= 12 -> { dialogMessage = "Nós vencemos a partida!"; gameOver = true }
            scoreB >= 12 -> { dialogMessage = "Eles venceram a partida..."; gameOver = true }
            scoreA == 11 -> dialogMessage = "Nós estamos na mão de 11!"
            scoreB == 11 -> dialogMessage = "Eles estão na mão de 11..."
        }
    }

    fun restartGame() {
        scoreA = 0
        scoreB = 0
        gameOver = false
        dialogMessage = null
    }

    dialogMessage?.let { message ->
        AlertDialog(
            onDismissRequest = { if (!gameOver) dialogMessage = null },
            title = { Text(if (gameOver) "Fim de Jogo!" else "Mão de 11!") },
            text = { Text(message) },
            confirmButton = {
                TextButton(onClick = {
                    if (gameOver) restartGame()
                    else dialogMessage = null
                }) {
                    Text(if (gameOver) "Reiniciar" else "OK")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "TrucoScoreBoard",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TeamPanel(
                teamName = "Nós",
                score = scoreA,
                enabled = !gameOver,
                onAddOne = { addPoints(teamA = true, points = 1) },
                onAddThree = { addPoints(teamA = true, points = 3) }
            )

            VerticalDivider(modifier = Modifier.height(200.dp))

            TeamPanel(
                teamName = "Eles",
                score = scoreB,
                enabled = !gameOver,
                onAddOne = { addPoints(teamA = false, points = 1) },
                onAddThree = { addPoints(teamA = false, points = 3) }
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Button(onClick = { restartGame() }) {
            Text("Reiniciar")
        }
    }
}

@Composable
fun TeamPanel(
    teamName: String,
    score: Int,
    enabled: Boolean,
    onAddOne: () -> Unit,
    onAddThree: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = teamName, fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = score.toString(), fontSize = 64.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onAddOne, enabled = enabled) {
            Text("+1")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onAddThree, enabled = enabled) {
            Text("+3")
        }
    }
}