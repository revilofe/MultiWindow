/**
 * Basado en https://github.com/JetBrains/compose-jb/tree/master/tutorials/Window_API_new#open-and-close-multiple-windows
 */

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    val estadoDeAplicacion = remember { EstadoDeAplicacion() }

    //Recorre todos los estados y pinta tantos componentes de ventana como estados hay.
    for (estadoDeVentana in estadoDeAplicacion.estadosDeVentana) {
        key(estadoDeVentana) {
            ComponenteVentana(estadoDeVentana) //Llama a la función composable para pintar una ventana, configurandolo según el estado estadoDeVentana
        }
    }
}

/**
 * Función composable que pinta un componente ventana, recibe como parámetro un estado de ventana
 * Este componente tiene un Menu Bar, con una opción de menu Archivo y dos items de menu Nueva Ventana, Salir
 *
 * El componente ventana queda configurado en base a su estado que recibe como parametro
 *
 * @param estado el estado de la ventana
 */
@Composable
@Preview
//private fun ApplicationScope.MiComponenteWindow(
private fun ComponenteVentana(
    estado: RepresentacionEstadoVentana
) = Window(onCloseRequest = estado::cierra, title = estado.titulo) {
    MenuBar {
        Menu("Archivo") {
            Item("Nueva ventana", onClick = estado.cuandoNuevaVentana)
            Item("Salir", onClick = estado.alSalir)
        }
    }
}

/**
 * La clase que gestiona el estado de la aplicación
 * Tiene una lista de estados de ventana que representan a cada una de las ventanas.
 */
private class EstadoDeAplicacion {
    val estadosDeVentana = mutableStateListOf<RepresentacionEstadoVentana>()

    /**
     * Inicializa la lista de estados de ventana, con la ventana principal
     */
    init {
        estadosDeVentana += CreaRepresentacionEstadoWindow("Initial window")
    }

    /**
     * genera un nuevo estado de ventana y lo añade a la lista
     *
     */
    fun nuevoEstadoDeVentana() {
        estadosDeVentana += CreaRepresentacionEstadoWindow("Window ${estadosDeVentana.size}")
    }

    /**
     * limpia la lista de estados
     *
     */
    fun limpiarEstadosDeVentana() {
        estadosDeVentana.clear()
    }

    /**
     * Método privado, se encarga de crear una representación del estado de una ventana
     *
     * @param El titulo de la ventana.
     * @return
     */
    private fun CreaRepresentacionEstadoWindow(
        titulo: String
    ) = RepresentacionEstadoVentana(
        titulo,
        cuandoNuevaVentana = ::nuevoEstadoDeVentana, //Se configura para que se llame al metodo EstadoDeAplicacion.nuevoEstadoDeVentana()
        alSalir = ::limpiarEstadosDeVentana, //Se configura para que se llame al metodo EstadoDeAplicacion.limpiarEstadosDeVentana()
        alCerrar = estadosDeVentana::remove  //Se configura para que se llame a elminar un estado de la lista de estados
    )
}

/**
 * Clase representación del estado de una ventana
 */
private class RepresentacionEstadoVentana(
    val titulo: String,
    val cuandoNuevaVentana: () -> Unit, //Que hace cuando se pulsa "Nueva ventana"
    val alSalir: () -> Unit, //Que hace cuando se pulsa "Salir"
    private val alCerrar: (RepresentacionEstadoVentana) -> Unit //Que hace cuando se cierra una ventana
) {
    fun cierra() = alCerrar(this)
}