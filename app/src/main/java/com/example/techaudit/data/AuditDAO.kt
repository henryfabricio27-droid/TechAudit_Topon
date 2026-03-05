package com.example.techaudit.data

import androidx.room.*
import com.example.techaudit.model.AuditItem
import com.example.techaudit.model.Laboratorio
import kotlinx.coroutines.flow.Flow

@Dao
interface AuditDAO {
    
    @Query("SELECT * FROM laboratorios ORDER BY nombre ASC")
    fun getAllLaboratorios(): Flow<List<Laboratorio>>

    @Query("SELECT * FROM laboratorios")
    suspend fun getAllLaboratoriosStatic(): List<Laboratorio>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLaboratorio(laboratorio: Laboratorio)

    @Delete
    suspend fun deleteLaboratorio(laboratorio: Laboratorio)


    @Query("UPDATE equipos SET laboratorioId = :newId WHERE laboratorioId = :oldId")
    suspend fun updateEquiposLabId(oldId: String, newId: String)

    @Query("SELECT * FROM equipos WHERE laboratorioId = :labId ORDER BY nombre ASC")
    fun getEquiposByLaboratorio(labId: String): Flow<List<AuditItem>>

    @Query("SELECT * FROM equipos")
    suspend fun getAllEquiposStatic(): List<AuditItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEquipo(item: AuditItem)

    @Update
    suspend fun updateEquipo(item: AuditItem)

    @Delete
    suspend fun deleteEquipo(item: AuditItem)
}
