package com.example.sonuslink

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sonuslink.ui.theme.SonusLinkTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SonusLinkTheme {
                val navController = rememberNavController()
                // El estado vive dentro del contexto Compose usando remember
                var usuarioLogueadoActual by remember { mutableStateOf<User?>(null) }

                NavHost(navController = navController, startDestination = "login") {
                    composable("login") {
                        LoginScreen(
                            onNavigateToRegistro = { navController.navigate("registro") },
                            onNavigateToRecuperar = { navController.navigate("recuperar") },
                            onLoginSuccess = { user ->
                                usuarioLogueadoActual = user
                                navController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        )
                    }
                    composable("registro") {
                        RegistroScreen(
                            onNavigateToLogin = { navController.navigate("login") }
                        )
                    }
                    composable("recuperar") {
                        RecuperarPasswordScreen(
                            onNavigateToLogin = { navController.navigate("login") }
                        )
                    }
                    composable("home") {
                        HomeScreen(
                            user = usuarioLogueadoActual,
                            onLogout = {
                                usuarioLogueadoActual = null
                                navController.navigate("login") {
                                    popUpTo("home") { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}