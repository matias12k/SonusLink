package com.example.sonuslink

import org.junit.Test
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse

class ValidatorTest {


    @Test
    fun email_valido_retornaTrue() {
        val email = "estudiante.duoc@correo.cl"
        assertTrue(Validator.isEmailValid(email))
    }

    @Test
    fun email_sinArroba_retornaFalse() {
        val email = "estudiantedwoc.cl"
        assertFalse(Validator.isEmailValid(email))
    }

    @Test
    fun email_vacio_retornaFalse() {
        assertFalse(Validator.isEmailValid(""))
    }

    @Test
    fun clave_valida_mayorOIgualA6_retornaTrue() {
        assertTrue(Validator.isPasswordSecure("123456"))
    }

    @Test
    fun clave_corta_menorA6_retornaFalse() {
        assertFalse(Validator.isPasswordSecure("12345"))
    }
}