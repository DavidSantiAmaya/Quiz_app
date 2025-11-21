package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.CartItem
import com.example.myapplication.data.repository.CartRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// -----------------------------------------------------------------------------
// VIEWMODEL DEL CARRITO (CartViewModel)
// -----------------------------------------------------------------------------
// Este ViewModel administra los datos relacionados con el carrito de compras.
// Accede al repositorio (CartRepository) y expone flujos (StateFlow) hacia la UI.
//
// ¿Por qué AndroidViewModel?
// - Porque necesita acceso al contexto del Application, el cual se usa en
//   CartRepository para inicializar la base de datos local.
// -----------------------------------------------------------------------------
class CartViewModel(app: Application) : AndroidViewModel(app) {

    // -------------------------------------------------------------------------
    // REPOSITORIO DEL CARRITO
    // -------------------------------------------------------------------------
    // Contiene todas las operaciones del carrito (consultas, inserciones, etc.)
    // y maneja la base de datos o almacenamiento local.
    // -------------------------------------------------------------------------
    private val cartRepo = CartRepository(app)

    // -------------------------------------------------------------------------
    // FLUJO: LISTA DE ITEMS EN EL CARRITO
    // -------------------------------------------------------------------------
    // cartItems es un StateFlow que siempre mantiene el estado actual del carrito.
    //
    // - stateIn() convierte el Flow del repositorio en un StateFlow.
    // - viewModelScope → ciclo de vida del ViewModel
    // - SharingStarted.Eagerly → el flujo comienza a observarse inmediatamente
    // - emptyList() → valor inicial
    // -------------------------------------------------------------------------
    val cartItems: StateFlow<List<CartItem>> = cartRepo.getCartItems()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    // -------------------------------------------------------------------------
    // FLUJO: TOTAL A PAGAR DEL CARRITO
    // -------------------------------------------------------------------------
    // totalAmount mantiene el cálculo del total en tiempo real.
    //
    // Esto permite que la UI observe este valor sin tener que recalcular nada.
    // -------------------------------------------------------------------------
    val totalAmount: StateFlow<Double> = cartRepo.getCartTotal()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = 0.0
        )

    // -------------------------------------------------------------------------
    // ACTUALIZAR CANTIDAD
    // -------------------------------------------------------------------------
    // Modifica la cantidad de un producto dentro del carrito.
    // viewModelScope.launch ejecuta la operación en corrutina.
    // -------------------------------------------------------------------------
    fun updateQuantity(productId: Int, qty: Int) {
        viewModelScope.launch {
            cartRepo.updateQuantity(productId, qty)
        }
    }

    // -------------------------------------------------------------------------
    // ELIMINAR UN ITEM DEL CARRITO
    // -------------------------------------------------------------------------
    fun removeItem(item: CartItem) {
        viewModelScope.launch {
            cartRepo.removeItem(item)
        }
    }

    // -------------------------------------------------------------------------
    // LIMPIAR EL CARRITO COMPLETO
    // -------------------------------------------------------------------------
    fun clearCart() {
        viewModelScope.launch {
            cartRepo.clearCart()
        }
    }
}
