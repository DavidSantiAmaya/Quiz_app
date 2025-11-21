package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.ProductRepository
import com.example.myapplication.model.ProductDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ProductListState(
    val products: List<ProductDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = ""
)

class ProductListViewModel : ViewModel() {
    private val repo = ProductRepository()
    private val _state = MutableStateFlow(ProductListState(isLoading = true))
    val productListState: StateFlow<ProductListState> = _state

    init {
        viewModelScope.launch {
            try {
                val list = repo.getProducts()
                _state.value = ProductListState(products = list, isLoading = false)
            } catch (e: Exception) {
                _state.value = ProductListState(error = "Error al cargar productos", isLoading = false)
            }
        }
    }
}
