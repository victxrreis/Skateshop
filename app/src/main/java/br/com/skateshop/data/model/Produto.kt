package br.com.skateshop.data.model

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "produtos")
data class Produto(
    @PrimaryKey val id: String,
    val nome: String,
    val descricao: String,
    val preco: Double,
    @DrawableRes val imagemResId: Int,
    val estoque: Int,
    val categoria: String,
    val medidas: String
)