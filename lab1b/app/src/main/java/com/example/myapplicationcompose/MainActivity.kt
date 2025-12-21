package com.example.myapplicationcompose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.BackHandler
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
                            ScreenA(onNext = {
                                Log.d("Navigation", "Переход с A на B")
                                navController.navigate("screenB")
                            }, activity = this@MainActivity)
                        }

                        composable("screenB") {
                            ScreenB(onNext = {
                                Log.d("Navigation", "Переход с B на C")
                                navController.navigate("screenC")
                            }, onBack = {
                                Log.d("Navigation", "Системная кнопка назад на B")
                                navController.popBackStack()
                            })
                        }

                        composable("screenC") {
                            ScreenC(onNext = {
                                Log.d("Navigation", "Переход с C на A")
                                navController.navigate("screenA") {
                                    popUpTo("screenA") { inclusive = true }
                                }
                            }, onBack = {
                                Log.d("Navigation", "Системная кнопка назад на C")
                                navController.popBackStack()
                            })
                        }
                    }
                }
            }
        }
    }
}
