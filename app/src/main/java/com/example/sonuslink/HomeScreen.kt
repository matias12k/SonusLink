package com.example.sonuslink

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(user: User?, onLogout: () -> Unit) {
    var textoMensaje by remember { mutableStateOf("") }
    val context = LocalContext.current

    val frasesFrecuentes = listOf(
        "Por favor, hable despacio o escriba aquí.",
        "Soy una persona con discapacidad auditiva.",
        "¿Dónde se encuentra la salida de emergencia?",
        "Muchas gracias por su apoyo y colaboración."
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Bienvenido/a, ${user?.nombre ?: "Usuario"}",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Grado auditivo: ${user?.nivelAudicion} | Lengua: ${user?.lenguaSenas}",
            fontSize = 13.sp,
            color = Color.DarkGray,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Panel de transcripción visual rápida
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Escribir para mostrar en pantalla grande:", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = textoMensaje,
                    onValueChange = { textoMensaje = it },
                    placeholder = { Text("Escriba su mensaje aquí...") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        if (textoMensaje.isNotBlank()) {
                            Toast.makeText(context, "Mostrando mensaje asistivo", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("MOSTRAR")
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // TABLA: Visualización de los usuarios en memoria (Array)
        Text("Usuarios registrados en memoria (Array en Kotlin):", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))

        // Encabezado de la Tabla
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(8.dp)
        ) {
            Text("Nombre", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.5f))
            Text("Correo", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.weight(2f))
            Text("Audición", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.2f))
        }

        // Filas de la Tabla desde el Array en Kotlin
        arrayUsuariosRegistrados.forEach { u ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(0.5.dp, Color.LightGray)
                    .padding(8.dp)
            ) {
                Text(u.nombre, fontSize = 12.sp, modifier = Modifier.weight(1.5f))
                Text(u.email, fontSize = 12.sp, modifier = Modifier.weight(2f))
                Text(u.nivelAudicion, fontSize = 12.sp, modifier = Modifier.weight(1.2f))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("Frases rápidas de asistencia:", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))
        frasesFrecuentes.forEach { frase ->
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(frase, modifier = Modifier.padding(12.dp), fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // BOTÓN: Cerrar Sesión
        OutlinedButton(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("CERRAR SESIÓN", color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}