package com.example.practico2moviles1.ui.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.example.practico2moviles1.R
import com.example.practico2moviles1.ui.model.Tablero

class TableroView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    cuerpoColor: Int = ContextCompat.getColor(context, R.color.colorCuerpoSerpiente),
    cabezaColor: Int = ContextCompat.getColor(context, R.color.colorCabezaSerpiente),
    comidaColor: Int = ContextCompat.getColor(context, R.color.colorComida)
) : View(context, attrs) {

    private val paintCuerpoSerpiente = Paint().apply { color = cuerpoColor }
    private val paintCabezaSerpiente = Paint().apply { color = cabezaColor }
    private val paintComida = Paint().apply { color = comidaColor }

    private lateinit var tablero: Tablero
    private var anchoPantalla = 0f
    private var altoPantalla = 0f

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        anchoPantalla = width.toFloat()
        altoPantalla = height.toFloat()
        inicializarTablero()
    }

    private fun inicializarTablero() {
        tablero = Tablero(anchoPantalla.toInt(), altoPantalla.toInt(), context)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        dibujarTablero(canvas)
    }

    private fun dibujarTablero(canvas: Canvas) {
        val anchoCelda = anchoPantalla / tablero.columnas
        val altoCelda = altoPantalla / tablero.filas

        for (fila in 0 until tablero.filas) {
            for (columna in 0 until tablero.columnas) {
                when (tablero.matriz[fila][columna]) {
                    1 -> dibujarCuerpoSerpiente(canvas, columna, fila, anchoCelda, altoCelda)
                    2 -> dibujarCabezaSerpiente(canvas, columna, fila, anchoCelda, altoCelda)
                    3 -> dibujarComida(canvas, columna, fila, anchoCelda, altoCelda)
                }
            }
        }
    }

    private fun dibujarCuerpoSerpiente(canvas: Canvas, columna: Int, fila: Int, anchoCelda: Float, altoCelda: Float) {
        // Dibuja el cuerpo de la serpiente con un color sólido (sin gradiente)
        canvas.drawRect(
            columna * anchoCelda, fila * altoCelda,
            (columna + 1) * anchoCelda, (fila + 1) * altoCelda,
            paintCuerpoSerpiente
        )
    }

    private fun dibujarCabezaSerpiente(canvas: Canvas, columna: Int, fila: Int, anchoCelda: Float, altoCelda: Float) {
        canvas.drawRect(
            columna * anchoCelda, fila * altoCelda,
            (columna + 1) * anchoCelda, (fila + 1) * altoCelda,
            paintCabezaSerpiente
        )
    }

    private fun dibujarComida(canvas: Canvas, columna: Int, fila: Int, anchoCelda: Float, altoCelda: Float) {
        canvas.drawRect(
            columna * anchoCelda, fila * altoCelda,
            (columna + 1) * anchoCelda, (fila + 1) * altoCelda,
            paintComida
        )
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            moverSerpiente(event.x, event.y)
            invalidate() // Redibuja la pantalla
        }
        return true
    }

    private fun moverSerpiente(touchX: Float, touchY: Float) {
        val mitadAncho = anchoPantalla / 2
        val mitadAlto = altoPantalla / 2

        when {
            touchY < mitadAlto / 2 -> tablero.moverSerpienteArriba()
            touchY > mitadAlto + (mitadAlto / 2) -> tablero.moverSerpienteAbajo()
            touchX < mitadAncho && touchY in (mitadAlto / 2)..(mitadAlto + (mitadAlto / 2)) -> tablero.moverSerpienteIzquierda()
            touchX > mitadAncho && touchY in (mitadAlto / 2)..(mitadAlto + (mitadAlto / 2)) -> tablero.moverSerpienteDerecha()
        }
    }
}
