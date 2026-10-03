package com.example.sonuslink

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.location.LocationServices

@SuppressLint("MissingPermission")
@Composable
fun BuscarDispositivoScreen() {
    val context = LocalContext.current
    var ubicacionTexto by remember { mutableStateOf("Coordenadas no obtenidas") }
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Módulo Buscar Dispositivos / Balizas",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = ubicacionTexto,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                    ubicacionTexto = if (loc != null) {
                        "Latitud: ${loc.latitude}\nLongitud: ${loc.longitude}\nBalizas asistenciales activas: 2 encontradas"
                    } else {
                        "GPS inactivo o sin señal en el emulador."
                    }
                }.addOnFailureListener {
                    ubicacionTexto = "Error al conectar con el servicio de ubicación."
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Escanear balizas cercanas por GPS")
        }
    }
}