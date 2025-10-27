package br.com.skateshop.data.repository

import br.com.skateshop.data.database.ProdutoDao
import br.com.skateshop.data.model.Produto
import kotlinx.coroutines.flow.Flow

class ProdutoRepository(private val produtoDao: ProdutoDao) {

    val todosProdutos: Flow<List<Produto>> = produtoDao.getTodosProdutos()

    fun getProdutoPorId(id: String): Flow<Produto> {
        return produtoDao.getProdutoPorId(id)
    }

    fun getCategorias(): List<String> = DadosIniciais.getCategorias()

    suspend fun insertProduto(produto: Produto) {
        produtoDao.insertProduto(produto)
    }

    suspend fun updateProduto(produto: Produto) {
        produtoDao.updateProduto(produto)
    }

    suspend fun deleteProduto(produto: Produto) {
        produtoDao.deleteProduto(produto)
    }
}