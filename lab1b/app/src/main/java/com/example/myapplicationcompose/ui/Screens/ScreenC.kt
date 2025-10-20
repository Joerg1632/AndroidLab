import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import android.util.Log
import androidx.compose.ui.Alignment

@Composable
fun ScreenC(onNext: () -> Unit, onBack: () -> Unit) {
    Log.d("Navigation", "Screen C shown")
    val buttonHeight = 56.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Magenta)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Экран C", color = Color.Black)
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                Log.d("Navigation", "Переходим с C на A")
                onNext()
            },
            modifier = Modifier.height(buttonHeight).fillMaxWidth(0.6f)
        ) {
            Text("Перейти на A")
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                Log.d("Navigation", "Переходим с C на B")
                onBack()
            },
            modifier = Modifier.height(buttonHeight).fillMaxWidth(0.6f)
        ) {
            Text("Назад")
        }
    }
}
