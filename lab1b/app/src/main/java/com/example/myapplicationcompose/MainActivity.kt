package com.example.myapplicationcompose
import ScreenA
import ScreenB
import ScreenC
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            MaterialTheme {
                Surface {
                    NavHost(navController = navController, startDestination = "screenA") {
                        composable("screenA") {
                            ScreenA(onNext = { navController.navigate("screenB") }, activity = this@MainActivity)
                        }
                        composable("screenB") {
                            ScreenB(
                                onNext = { navController.navigate("screenC") },
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("screenC") {
                            ScreenC(
                                onNext = { navController.navigate("screenA") },
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}