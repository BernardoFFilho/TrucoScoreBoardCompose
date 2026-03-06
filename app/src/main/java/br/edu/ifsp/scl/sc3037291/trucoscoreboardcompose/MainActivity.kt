package br.edu.ifsp.scl.sc3037291.trucoscoreboardcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DynamicThemeWrapper {
                TrucoScoreBoardApp()
            }
        }
    }
}

@Composable
fun DynamicThemeWrapper(content: @Composable () -> Unit) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val darkTheme = isSystemInDarkTheme()
    val colorScheme = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    } else {
        if (darkTheme) darkColorScheme() else lightColorScheme()
    }
    MaterialTheme(colorScheme = colorScheme, content = content)
}

@Composable
fun TrucoScoreBoardApp() {
    var scoreA by remember { mutableIntStateOf(0) }
    var scoreB by remember { mutableIntStateOf(0) }
    var roundValue by remember { mutableIntStateOf(1) }
    var dialogMessage by remember { mutableStateOf<String?>(null) }
    var gameOver by remember { mutableStateOf(false) }
    var flashA by remember { mutableStateOf(false) }
    var flashB by remember { mutableStateOf(false) }

    val trucoValues = listOf(1, 3, 6, 9, 12)

    fun nextTrucoValue(): Int {
        val currentIndex = trucoValues.indexOf(roundValue)
        return if (currentIndex < trucoValues.size - 1) trucoValues[currentIndex + 1] else 12
    }

    fun teamWins(teamA: Boolean) {
        if (gameOver) return
        if (teamA) {
            scoreA = minOf(scoreA + roundValue, 12)
            flashA = true
        } else {
            scoreB = minOf(scoreB + roundValue, 12)
            flashB = true
        }
        roundValue = 1
        when {
            scoreA >= 12 -> { dialogMessage = "Nós vencemos a partida!"; gameOver = true }
            scoreB >= 12 -> { dialogMessage = "Eles venceram a partida..."; gameOver = true }
            scoreA == 11 -> dialogMessage = "Nós estamos na mão de 11!"
            scoreB == 11 -> dialogMessage = "Eles estão na mão de 11..."
        }
    }

    fun restartGame() {
        scoreA = 0; scoreB = 0; roundValue = 1; gameOver = false; dialogMessage = null
    }

    LaunchedEffect(flashA) { if (flashA) { delay(400); flashA = false } }
    LaunchedEffect(flashB) { if (flashB) { delay(400); flashB = false } }

    dialogMessage?.let { message ->
        AlertDialog(
            onDismissRequest = { if (!gameOver) dialogMessage = null },
            title = { Text(if (gameOver) "🏆 Fim de Jogo!" else "⚠️ Mão de 11!") },
            text = { Text(message, fontSize = 16.sp) },
            confirmButton = {
                TextButton(onClick = {
                    if (gameOver) restartGame() else dialogMessage = null
                }) { Text(if (gameOver) "Reiniciar" else "OK") }
            }
        )
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            // Título
            Text(
                text = "🃏 Truco!",
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )

            // Placar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TeamCard(
                    teamName = "Nós",
                    score = scoreA,
                    flash = flashA,
                    enabled = !gameOver,
                    accentColor = MaterialTheme.colorScheme.primary,
                    onWin = { teamWins(teamA = true) }
                )

                Text(
                    text = "✕",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )

                TeamCard(
                    teamName = "Eles",
                    score = scoreB,
                    flash = flashB,
                    enabled = !gameOver,
                    accentColor = MaterialTheme.colorScheme.error,
                    onWin = { teamWins(teamA = false) }
                )
            }

            // Valor da rodada + Truco
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Rodada vale",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                    )
                    Text(
                        text = "$roundValue ponto${if (roundValue > 1) "s" else ""}",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { roundValue = nextTrucoValue() },
                        enabled = !gameOver && roundValue < 12,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary
                        )
                    ) {
                        Text(
                            text = when (roundValue) {
                                1 -> "🗣️ Truco!"
                                3 -> "🔥 Seis!"
                                6 -> "💥 Nove!"
                                9 -> "⚡ Doze!"
                                else -> "✅ Máximo"
                            },
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Reiniciar
            OutlinedButton(
                onClick = { restartGame() },
                modifier = Modifier.fillMaxWidth(0.6f)
            ) {
                Text("🔄 Reiniciar", fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun TeamCard(
    teamName: String,
    score: Int,
    flash: Boolean,
    enabled: Boolean,
    accentColor: Color,
    onWin: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (flash) 1.15f else 1f,
        animationSpec = tween(200),
        label = "scale"
    )
    val bgColor by animateColorAsState(
        targetValue = if (flash) accentColor.copy(alpha = 0.2f) else Color.Transparent,
        animationSpec = tween(300),
        label = "bgColor"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .border(2.dp, accentColor.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
            .padding(16.dp)
            .scale(scale)
    ) {
        Text(
            text = teamName,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = accentColor
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = score.toString(),
            fontSize = 80.sp,
            fontWeight = FontWeight.ExtraBold,
            color = accentColor
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = onWin,
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(containerColor = accentColor),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.width(120.dp).height(48.dp)
        ) {
            Text("Ganhou! 🎉", fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
    }
}