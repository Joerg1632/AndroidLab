package com.example.myapplicationcompose

import android.util.Log
import androidx.activity.ComponentActivity
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
fun ScreenA(onNext: () -> Unit, activity: ComponentActivity) {
    BackHandler {
        Log.d("Navigation", "Системная кнопка назад на A — закрываем приложение")
        activity.finishAffinity()
    }

    val buttonHeight = 56.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Green)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Экран A", color = Color.Black)

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = onNext,
            modifier = Modifier.height(buttonHeight).fillMaxWidth(0.6f)
        ) {
            Text("Перейти на B")
        }
    }
}
