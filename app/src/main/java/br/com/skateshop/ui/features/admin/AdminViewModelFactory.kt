package br.com.skateshop.ui.features.admin

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.skateshop.data.database.AppDatabase
import br.com.skateshop.data.repository.ProdutoRepository

class AdminViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdminViewModel::class.java)) {
            val dao = AppDatabase.getDatabase(context).produtoDao()
            val repository = ProdutoRepository(dao)

            @Suppress("UNCHECKED_CAST")
            return AdminViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}