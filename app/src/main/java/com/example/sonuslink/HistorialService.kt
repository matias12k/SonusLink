package com.example.sonuslink

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

// Modelo de datos para cada registro
data class MensajeHistorial(
    val id: String = "",
    val uid: String = "",
    val texto: String = "",
    val tipo: String = "", // "Texto a Voz" o "Voz a Texto"
    val timestamp: Long = System.currentTimeMillis()
)

class HistorialService(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {

    // Guardar un nuevo registro en Firestore
    fun guardarMensaje(texto: String, tipo: String, onComplete: (Boolean, String?) -> Unit) {
        val uidActual = auth.currentUser?.uid ?: "anonimo"

        val nuevoRegistro = hashMapOf(
            "uid" to uidActual,
            "texto" to texto,
            "tipo" to tipo,
            "timestamp" to System.currentTimeMillis()
        )

        db.collection("historial")
            .add(nuevoRegistro)
            .addOnSuccessListener { onComplete(true, null) }
            .addOnFailureListener { e -> onComplete(false, e.message) }
    }

    // Escuchar el historial del usuario actual en tiempo real
    fun escucharHistorial(onUpdate: (List<MensajeHistorial>) -> Unit) {
        val uidActual = auth.currentUser?.uid ?: return

        db.collection("historial")
            .whereEqualTo("uid", uidActual)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    onUpdate(emptyList())
                    return@addSnapshotListener
                }

                val lista = snapshot.documents.map { doc ->
                    MensajeHistorial(
                        id = doc.id,
                        uid = doc.getString("uid") ?: "",
                        texto = doc.getString("texto") ?: "",
                        tipo = doc.getString("tipo") ?: "",
                        timestamp = doc.getLong("timestamp") ?: 0L
                    )
                }
                onUpdate(lista)
            }
    }
}