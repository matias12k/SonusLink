package com.example.sonuslink

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sonuslink.User
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(
    onNavigateToRegistro: () -> Unit,
    onNavigateToRecuperar: () -> Unit,
    onLoginSuccess: (User) -> Unit
) {
    var emailInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var estaCargando by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val authService = remember { AuthService() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "SonusLink",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Accesibilidad e Inclusión Auditiva",
            fontSize = 13.sp,
            color = Color.DarkGray,
            modifier = Modifier.padding(bottom = 28.dp)
        )

        // INPUT: Correo institucional
        OutlinedTextField(
            value = emailInput,
            onValueChange = { emailInput = it },
            label = { Text("Correo Institucional") },
            placeholder = { Text("ejemplo@duocuc.cl") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            singleLine = true,
            enabled = !estaCargando,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // INPUT: Contraseña
        OutlinedTextField(
            value = passwordInput,
            onValueChange = { passwordInput = it },
            label = { Text("Contraseña") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            enabled = !estaCargando,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // BOTÓN: Iniciar Sesión con Firebase Authentication
        Button(
            onClick = {
                val emailLimpio = emailInput.trim()
                val passLimpio = passwordInput.trim()

                if (emailLimpio.isEmpty() || passLimpio.isEmpty()) {
                    Toast.makeText(context, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                estaCargando = true
                authService.loginUsuario(emailLimpio, passLimpio) { exitoso, error ->
                    estaCargando = false
                    if (exitoso) {
                        val usuarioAuth = FirebaseAuth.getInstance().currentUser
                        // Extrae el nombre del correo o displayName si existe
                        val nombreMostrar = usuarioAuth?.displayName
                            ?: emailLimpio.substringBefore("@").replaceFirstChar { it.uppercase() }

                        val usuarioSesion = User(
                            nombre = nombreMostrar,
                            email = emailLimpio,
                            pass = passLimpio
                        )

                        Toast.makeText(context, "Bienvenido/a, $nombreMostrar", Toast.LENGTH_SHORT).show()
                        onLoginSuccess(usuarioSesion)
                    } else {
                        val mensaje = when {
                            error?.contains("invalid-credential", ignoreCase = true) == true ||
                                    error?.contains("user-not-found", ignoreCase = true) == true ||
                                    error?.contains("wrong-password", ignoreCase = true) == true ->
                                "Credenciales incorrectas. Verifique correo o clave."
                            error?.contains("network", ignoreCase = true) == true ->
                                "Error de conexión. Revise su conexión a Internet."
                            else -> error ?: "Error al autenticar usuario"
                        }
                        Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show()
                    }
                }
            },
            enabled = !estaCargando,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            if (estaCargando) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.5.dp
                )
            } else {
                Text("INICIAR SESIÓN", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // VÍNCULOS
        TextButton(
            onClick = onNavigateToRecuperar,
            enabled = !estaCargando
        ) {
            Text("¿Olvidó su contraseña?", color = MaterialTheme.colorScheme.primary)
        }

        TextButton(
            onClick = onNavigateToRegistro,
            enabled = !estaCargando
        ) {
            Text("¿No tiene cuenta? Regístrese aquí", fontWeight = FontWeight.SemiBold)
        }
    }
}
