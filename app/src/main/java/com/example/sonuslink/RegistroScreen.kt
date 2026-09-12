package com.example.sonuslink

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(onNavigateToLogin: () -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // CHECK LIST: Preferencias sensoriales
    var checkVibracion by remember { mutableStateOf(true) }
    var checkLuzFlash by remember { mutableStateOf(true) }

    // RADIO BUTTONS: Grado de audición
    val opcionesAudicion = listOf("Leve", "Moderada", "Severa / Profunda")
    var audicionSeleccionada by remember { mutableStateOf(opcionesAudicion[1]) }

    // COMBO BOX: Lengua de señas
    val idiomasSenas = listOf("Chilena (LSCh)", "Internacional (IS)", "Americana (ASL)")
    var isComboExpanded by remember { mutableStateOf(false) }
    var idiomaSeleccionado by remember { mutableStateOf(idiomasSenas[0]) }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registro Accesible", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateToLogin) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start
        ) {
            // INPUT: Nombre
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre Completo") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // INPUT: Correo
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo Electrónico") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // INPUT: Contraseña
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // RADIO BUTTONS: Grado de hipoacusia
            Text("Grado de Discapacidad Auditiva:", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            opcionesAudicion.forEach { nivel ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (nivel == audicionSeleccionada),
                            onClick = { audicionSeleccionada = nivel }
                        )
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (nivel == audicionSeleccionada),
                        onClick = { audicionSeleccionada = nivel }
                    )
                    Text(text = nivel, modifier = Modifier.padding(start = 8.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // CHECK LIST: Preferencias sensoriales
            Text("Alertas sensoriales integradas:", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = checkVibracion, onCheckedChange = { checkVibracion = it })
                Text("Activar respuesta por vibración (háptica)")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = checkLuzFlash, onCheckedChange = { checkLuzFlash = it })
                Text("Activar destellos lumínicos para avisos")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // COMBO BOX (ExposedDropdownMenuBox)
            Text("Lengua de señas preferente:", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Spacer(modifier = Modifier.height(6.dp))
            ExposedDropdownMenuBox(
                expanded = isComboExpanded,
                onExpandedChange = { isComboExpanded = !isComboExpanded }
            ) {
                OutlinedTextField(
                    value = idiomaSeleccionado,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isComboExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = isComboExpanded,
                    onDismissRequest = { isComboExpanded = false }
                ) {
                    idiomasSenas.forEach { seleccion ->
                        DropdownMenuItem(
                            text = { Text(seleccion) },
                            onClick = {
                                idiomaSeleccionado = seleccion
                                isComboExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // BOTÓN GUARDAR
            Button(
                onClick = {
                    if (nombre.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
                        Toast.makeText(context, "Registro de $nombre completado con éxito.", Toast.LENGTH_LONG).show()
                        onNavigateToLogin()
                    } else {
                        Toast.makeText(context, "Por favor rellene todos los campos del formulario.", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("GUARDAR Y CONFIRMAR REGISTRO", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}