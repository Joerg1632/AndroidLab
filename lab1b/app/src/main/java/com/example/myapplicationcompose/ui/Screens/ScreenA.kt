import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.activity.ComponentActivity

@Composable
fun ScreenA(onNext: () -> Unit, activity: ComponentActivity) {
    Log.d("Navigation", "Screen A shown")
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
            onClick = {
                Log.d("Navigation", "Переходим с A на B")
                onNext()
            },
            modifier = Modifier.height(buttonHeight).fillMaxWidth(0.6f)
        ) {
            Text("Перейти на B")
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                Log.d("Navigation", "Закрываем приложение")
                activity.finish()
            },
            modifier = Modifier.height(buttonHeight).fillMaxWidth(0.6f)
        ) {
            Text("Назад")
        }
    }
}
