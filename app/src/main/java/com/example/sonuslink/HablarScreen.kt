package com.example.sonuslink

import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HablarScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    var texto by remember { mutableStateOf("") }
    var tts: TextToSpeech? by remember { mutableStateOf(null) }
    var guardandoEnFirebase by remember { mutableStateOf(false) }

    val historialService = remember { HistorialService() }

    DisposableEffect(Unit) {
        val ttsInstance = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.forLanguageTag("es-ES")
            }
        }
        tts = ttsInstance
        onDispose {
            ttsInstance.stop()
            ttsInstance.shutdown()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Módulo Hablar", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Módulo Hablar (Texto a Voz)", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = texto,
                onValueChange = { texto = it },
                label = { Text("Escribe lo que deseas decir...") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    val textoParaReproducir = texto.trim()
                    if (textoParaReproducir.isNotBlank()) {
                        tts?.speak(textoParaReproducir, TextToSpeech.QUEUE_FLUSH, null, "TTS_ID")

                        guardandoEnFirebase = true
                        historialService.guardarMensaje(
                            texto = textoParaReproducir,
                            tipo = "Texto a Voz"
                        ) { exitoso, error ->
                            guardandoEnFirebase = false
                            if (exitoso) {
                                Toast.makeText(context, "Guardado en historial", Toast.LENGTH_SHORT).show()
                                texto = ""
                            } else {
                                Toast.makeText(context, "Error al sincronizar: $error", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(context, "Ingrese un texto para reproducir", Toast.LENGTH_SHORT).show()
                    }
                },
                enabled = !guardandoEnFirebase,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (guardandoEnFirebase) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Reproducir en voz alta")
                }
            }
        }
    }
}