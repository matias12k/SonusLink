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
import com.example.sonuslink.arrayUsuariosRegistrados

@Composable
fun LoginScreen(
    onNavigateToRegistro: () -> Unit,
    onNavigateToRecuperar: () -> Unit,
    onLoginSuccess: (User) -> Unit
) {
    var emailInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    val context = LocalContext.current

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
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // BOTÓN: Iniciar Sesión (Valida contra el Array en memoria)
        Button(
            onClick = {
                val match = arrayUsuariosRegistrados.find {
                    it.email.equals(emailInput.trim(), ignoreCase = true) && it.pass == passwordInput
                }
                if (match != null) {
                    Toast.makeText(context, "Bienvenido/a, ${match.nombre}", Toast.LENGTH_SHORT).show()
                    onLoginSuccess(match)
                } else {
                    Toast.makeText(context, "Credenciales incorrectas. Verifique correo o clave.", Toast.LENGTH_LONG).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("INICIAR SESIÓN", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // VÍNCULOS
        TextButton(onClick = onNavigateToRecuperar) {
            Text("¿Olvidó su contraseña?", color = MaterialTheme.colorScheme.primary)
        }

        TextButton(onClick = onNavigateToRegistro) {
            Text("¿No tiene cuenta? Regístrese aquí", fontWeight = FontWeight.SemiBold)
        }
    }
}