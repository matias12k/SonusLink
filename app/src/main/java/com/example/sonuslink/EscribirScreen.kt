package com.example.sonuslink

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.Locale

@Composable
fun EscribirScreen() {
    val context = LocalContext.current
    var textoTranscrito by remember { mutableStateOf("El texto transcrito aparecerá aquí...") }
    var guardandoEnFirebase by remember { mutableStateOf(false) }

    // Instancia del servicio de Firestore
    val historialService = remember { HistorialService() }

    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val matches = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (!matches.isNullOrEmpty()) {
                val textoObtenido = matches[0]
                textoTranscrito = textoObtenido

                // Guardar la transcripción automáticamente en Firestore
                guardandoEnFirebase = true
                historialService.guardarMensaje(
                    texto = textoObtenido,
                    tipo = "Voz a Texto"
                ) { exitoso, error ->
                    guardandoEnFirebase = false
                    if (exitoso) {
                        Toast.makeText(context, "Transcripción guardada en historial", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Error al sincronizar: $error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Módulo Escribir (Voz a Texto)",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(20.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                if (guardandoEnFirebase) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Sincronizando...", style = MaterialTheme.typography.bodySmall)
                    }
                } else {
                    Text(
                        text = textoTranscrito,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                    )
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.forLanguageTag("es-ES"))
                    putExtra(RecognizerIntent.EXTRA_PROMPT, "Hable claro frente al micrófono...")
                }
                speechLauncher.launch(intent)
            },
            enabled = !guardandoEnFirebase,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Iniciar escucha por voz")
        }
    }
}