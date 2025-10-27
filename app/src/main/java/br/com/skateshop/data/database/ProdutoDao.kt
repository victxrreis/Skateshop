package br.com.skateshop.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import br.com.skateshop.data.model.Produto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProdutoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(produtos: List<Produto>)

    @Query("SELECT * FROM produtos ORDER BY nome ASC")
    fun getTodosProdutos(): Flow<List<Produto>>

    @Query("SELECT * FROM produtos WHERE id = :produtoId")
    fun getProdutoPorId(produtoId: String): Flow<Produto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduto(produto: Produto) // Insere/Substitui um produto

    @Update
    suspend fun updateProduto(produto: Produto) // Atualiza um produto

    @Delete
    suspend fun deleteProduto(produto: Produto) // Deleta um produto
}