package com.example.practico2moviles1.ui.model

import android.app.AlertDialog
import android.content.Context
import com.example.practico2moviles1.ui.activities.MainActivity
import kotlin.random.Random

class Tablero(private val anchoPantalla: Int, private val altoPantalla: Int, private val context: Context) {


    var filas = 20
    var columnas = (anchoPantalla.toFloat() / (altoPantalla.toFloat() / filas)).toInt()

    var matriz = Array(filas) { Array(columnas) { 0 } }

    private var posicionCabezaX = columnas / 2
    private var posicionCabezaY = filas / 2
    private var partesCuerpo = mutableListOf(Pair(posicionCabezaX, posicionCabezaY - 1),)

    private var comidaX = Random.nextInt(columnas)
    private var comidaY = Random.nextInt(filas)

    // Dirección actual de la serpiente
    private var direccionActual: Direccion? = null

    enum class Direccion {
        ARRIBA, ABAJO, IZQUIERDA, DERECHA
    }

    init {
        inicializarSerpiente()
    }

    fun inicializarSerpiente() {
        matriz = Array(filas) { Array(columnas) { 0 } }
        matriz[posicionCabezaY][posicionCabezaX] = 2

        for (parte in partesCuerpo) {
            matriz[parte.second][parte.first] = 1
        }
        comidaX = 7
        comidaY = 15

        if (matriz[comidaY][comidaX] != 0) {
            generarComida()
        } else {
            matriz[comidaY][comidaX] = 3
        }
    }
    private fun mover(nuevaCabezaX: Int, nuevaCabezaY: Int) {
        var nuevaX = nuevaCabezaX
        var nuevaY = nuevaCabezaY

        if (nuevaX >= columnas) nuevaX = 0
        if (nuevaX < 0) nuevaX = columnas - 1
        if (nuevaY >= filas) nuevaY = 0
        if (nuevaY < 0) nuevaY = filas - 1

        if (partesCuerpo.contains(Pair(nuevaX, nuevaY))) {
            mostrarGameOverDialog()
            return
        }

        // Verificar si la serpiente come la comida
        if (nuevaX == comidaX && nuevaY == comidaY) {
            partesCuerpo.add(0, Pair(posicionCabezaX, posicionCabezaY))
            generarComida() // Generar nueva comida
        } else {
            partesCuerpo.add(0, Pair(posicionCabezaX, posicionCabezaY))
            partesCuerpo.removeAt(partesCuerpo.size - 1)
        }

        posicionCabezaX = nuevaX
        posicionCabezaY = nuevaY

        // Limpiar y actualizar la matriz
        matriz = Array(filas) { Array(columnas) { 0 } }
        matriz[posicionCabezaY][posicionCabezaX] = 2

        for (parte in partesCuerpo) {
            matriz[parte.second][parte.first] = 1
        }

        matriz[comidaY][comidaX] = 3
    }

    private fun mostrarGameOverDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Game Over")
        builder.setMessage("La serpiente se ha chocado con su propio cuerpo.")
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            (context as MainActivity).finish()
        }
        builder.setCancelable(false)
        builder.show()
    }

    fun generarComida() {
        val posicionesOcupadas = mutableSetOf<Pair<Int, Int>>()

        // Agregar todas las posiciones del cuerpo de la serpiente al conjunto
        partesCuerpo.forEach { parte ->
            posicionesOcupadas.add(parte)
        }
        posicionesOcupadas.add(Pair(posicionCabezaX, posicionCabezaY)) // Agregar la cabeza de la serpiente
        // Generar comida en una posición aleatoria no ocupada
        do {
            comidaX = Random.nextInt(columnas)
            comidaY = Random.nextInt(filas)
        } while (posicionesOcupadas.contains(Pair(comidaX, comidaY)))

        matriz[comidaY][comidaX] = 3
    }

    fun moverSerpienteArriba() {
        if (direccionActual != Direccion.ABAJO) {
            mover(posicionCabezaX, posicionCabezaY - 1)
            direccionActual = Direccion.ARRIBA
        }
    }

    fun moverSerpienteAbajo() {
        if (direccionActual != Direccion.ARRIBA) {
            mover(posicionCabezaX, posicionCabezaY + 1)
            direccionActual = Direccion.ABAJO
        }
    }

    fun moverSerpienteIzquierda() {
        if (direccionActual != Direccion.DERECHA) {
            mover(posicionCabezaX - 1, posicionCabezaY)
            direccionActual = Direccion.IZQUIERDA
        }
    }

    fun moverSerpienteDerecha() {
        if (direccionActual != Direccion.IZQUIERDA) {
            mover(posicionCabezaX + 1, posicionCabezaY)
            direccionActual = Direccion.DERECHA
        }
    }
}
