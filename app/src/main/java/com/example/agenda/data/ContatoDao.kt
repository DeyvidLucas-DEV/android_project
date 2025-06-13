package com.example.agenda.data

import androidx.room.*
import com.example.agenda.model.Contato
import kotlinx.coroutines.flow.Flow

@Dao
interface ContatoDao {
    @Query("SELECT * FROM contatos ORDER BY nome ASC")
    fun getAllContatos(): Flow<List<Contato>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(contato: Contato): Long

    @Update
    suspend fun atualizar(contato: Contato)

    @Delete
    suspend fun deletar(contato: Contato)

    @Query("SELECT * FROM contatos WHERE id = :id")
    suspend fun getContatoById(id: Long): Contato?

    @Query("SELECT * FROM contatos WHERE nome LIKE '%' || :query || '%' OR telefone LIKE '%' || :query || '%'")
    fun buscarContatos(query: String): Flow<List<Contato>>
} 