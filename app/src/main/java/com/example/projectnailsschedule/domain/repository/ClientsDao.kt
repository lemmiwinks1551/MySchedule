package com.example.projectnailsschedule.domain.repository

import androidx.room.*
import com.example.projectnailsschedule.domain.models.ClientModelDb
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientsDao {
    @Insert
    suspend fun insert(clientModelDb: ClientModelDb)

    @Query("SELECT * FROM clients")
    fun selectAllFlow(): Flow<List<ClientModelDb>>

    @Update
    suspend fun update(clientModelDb: ClientModelDb)

    @Delete
    suspend fun delete(clientModelDb: ClientModelDb)

    @Query("SELECT * FROM clients")
    suspend fun selectAllClients(): List<ClientModelDb>

    @Query("SELECT * FROM clients WHERE " +
            "name LIKE :searchQuery OR " +
            "phone LIKE :searchQuery OR " +
            "notes LIKE :searchQuery order by name")
    fun searchClient(searchQuery: String): Flow<List<ClientModelDb>>
}