package com.example.sonuslink

data class User(
    val nombre: String,
    val email: String,
    val pass: String,
    val nivelAudicion: String = "Moderada",
    val usaAlertasVisuales: Boolean = true,
    val lenguaSenas: String = "Chilena (LSCh)"
)

val arrayUsuariosRegistrados: Array<User> = arrayOf(
    User("Matias Barraza", "matias@duocuc.cl", "Admin123*", "Moderada", true, "Chilena (LSCh)"),
    User("Camila Soto", "c.soto@duocuc.cl", "Cami2026!", "Severa / Profunda", true, "Internacional (IS)"),
    User("Diego Morales", "d.morales@duocuc.cl", "DiegoPass#", "Leve", false, "Chilena (LSCh)"),
    User("Fernanda Rivas", "f.rivas@duocuc.cl", "FerAudio99", "Severa / Profunda", true, "Americana (ASL)"),
    User("Rodrigo Silva", "r.silva@duocuc.cl", "RodrigoSafe1", "Moderada", true, "Chilena (LSCh)")
)