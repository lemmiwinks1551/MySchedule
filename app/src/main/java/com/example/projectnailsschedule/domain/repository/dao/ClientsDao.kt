package com.example.projectnailsschedule.domain.repository.dao

import androidx.room.*
import com.example.projectnailsschedule.domain.models.ClientModelDb
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientsDao {
    @Insert
    suspend fun insert(clientModelDb: ClientModelDb) : Long

    @Update
    suspend fun update(clientModelDb: ClientModelDb)

    @Delete
    suspend fun delete(clientModelDb: ClientModelDb)

    @Query("SELECT * FROM clients WHERE " +
            "name LIKE :searchQuery OR " +
            "phone LIKE :searchQuery OR " +
            "telegram LIKE :searchQuery OR " +
            "instagram LIKE :searchQuery OR " +
            "vk LIKE :searchQuery OR " +
            "whatsapp LIKE :searchQuery OR " +
            "notes LIKE :searchQuery order by name")
    fun searchClient(searchQuery: String): MutableList<ClientModelDb>

    @Query("SELECT * FROM clients WHERE _id = :id")
    suspend fun getClientById(id: Long): ClientModelDb
}