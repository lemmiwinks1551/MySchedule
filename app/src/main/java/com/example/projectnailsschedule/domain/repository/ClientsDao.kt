package com.example.projectnailsschedule.domain.repository

import androidx.room.*
import com.example.projectnailsschedule.domain.models.ClientModelDb
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientsDao {
    @Insert
    fun insert(clientModelDb: ClientModelDb)

    @Query("SELECT * FROM clients")
    fun selectAll(): Flow<List<ClientModelDb>>

    @Update
    fun update(clientModelDb: ClientModelDb)

    @Delete
    fun delete(clientModelDb: ClientModelDb)

    @Query("SELECT * FROM clients")
    fun selectAllList(): List<ClientModelDb>

    @Query("SELECT * FROM clients WHERE " +
            "name LIKE :searchQuery OR " +
            "phone LIKE :searchQuery OR " +
            "notes LIKE :searchQuery")
    fun searchDatabase(searchQuery: String): Flow<List<ClientModelDb>>
}