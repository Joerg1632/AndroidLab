package com.example.myapplicationcompose

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ScreenB(onNext: () -> Unit, onBack: () -> Unit) {
    BackHandler {
        onBack()
    }

    val buttonHeight = 56.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Cyan)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Экран B", color = Color.Black)

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = onNext,
            modifier = Modifier.height(buttonHeight).fillMaxWidth(0.6f)
        ) {
            Text("Перейти на C")
        }
    }
}
