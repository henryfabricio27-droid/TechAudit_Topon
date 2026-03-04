package com.example.techaudit.data

import androidx.room.*
import com.example.techaudit.model.AuditItem
import com.example.techaudit.model.Laboratorio
import kotlinx.coroutines.flow.Flow

@Dao
interface AuditDAO {
    
    // --- CONSULTAS PARA LABORATORIOS ---
    @Query("SELECT * FROM laboratorios ORDER BY nombre ASC")
    fun getAllLaboratorios(): Flow<List<Laboratorio>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLaboratorio(laboratorio: Laboratorio)

    @Delete
    suspend fun deleteLaboratorio(laboratorio: Laboratorio)

    // --- CONSULTAS PARA EQUIPOS ---
    
    // Traer equipos de un laboratorio específico
    @Query("SELECT * FROM equipos WHERE laboratorioId = :labId ORDER BY nombre ASC")
    fun getEquiposByLaboratorio(labId: String): Flow<List<AuditItem>>

    @Query("SELECT * FROM equipos")
    suspend fun getAllEquiposStatic(): List<AuditItem> // Útil para sincronización

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEquipo(item: AuditItem)

    @Update
    suspend fun updateEquipo(item: AuditItem)

    @Delete
    suspend fun deleteEquipo(item: AuditItem)
}
