package com.example.projectnailsschedule.domain.repository

import androidx.room.*
import com.example.projectnailsschedule.domain.models.ProcedureModelDb
import kotlinx.coroutines.flow.Flow

@Dao
interface ProcedureDao {
    @Insert
    suspend fun insert(procedureModelDb: ProcedureModelDb)

    @Update
    suspend fun update(procedureModelDb: ProcedureModelDb)

    @Delete
    suspend fun delete(procedureModelDb: ProcedureModelDb)

    @Query("SELECT * FROM procedures WHERE " +
            "procedureName LIKE :searchQuery OR " +
            "procedurePrice LIKE :searchQuery")
    fun searchDatabase(searchQuery: String): Flow<List<ProcedureModelDb>>
}