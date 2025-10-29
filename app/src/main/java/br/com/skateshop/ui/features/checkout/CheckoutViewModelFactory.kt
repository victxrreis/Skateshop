package br.com.skateshop.ui.features.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CheckoutViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CheckoutViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CheckoutViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}