package com.example.sonuslink

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthService(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    // Registro de usuario en Auth y almacenamiento de perfil en Firestore
    fun registrarUsuario(
        email: String,
        clave: String,
        nombre: String,
        onComplete: (Boolean, String?) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, clave)
            .addOnSuccessListener { resultado ->
                val uid = resultado.user?.uid ?: ""
                val perfil = hashMapOf(
                    "uid" to uid,
                    "nombre" to nombre,
                    "email" to email,
                    "tipo" to "Discapacidad Auditiva"
                )
                db.collection("usuarios").document(uid).set(perfil)
                    .addOnSuccessListener { onComplete(true, null) }
                    .addOnFailureListener { e -> onComplete(false, e.message) }
            }
            .addOnFailureListener { e -> onComplete(false, e.message) }
    }

    // Inicio de sesión
    fun loginUsuario(
        email: String,
        clave: String,
        onComplete: (Boolean, String?) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, clave)
            .addOnSuccessListener { onComplete(true, null) }
            .addOnFailureListener { e -> onComplete(false, e.message) }
    }

    // Recuperación de contraseña por correo
    fun recuperarClave(
        email: String,
        onComplete: (Boolean, String?) -> Unit
    ) {
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener { onComplete(true, null) }
            .addOnFailureListener { e -> onComplete(false, e.message) }
    }
}