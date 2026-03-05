package com.example.techaudit.data

import android.util.Log
import com.example.techaudit.model.AuditItem
import com.example.techaudit.model.Laboratorio
import kotlinx.coroutines.flow.Flow

class AuditRepository(
    private val auditDao: AuditDAO,
    private val apiService: ApiService
) {

    val allLaboratorios: Flow<List<Laboratorio>> = auditDao.getAllLaboratorios()

    suspend fun insertLaboratorio(laboratorio: Laboratorio) = auditDao.insertLaboratorio(laboratorio)
    
    // --- BORRADO BIDIRECCIONAL LABORATORIO ---
    suspend fun deleteLaboratorio(laboratorio: Laboratorio) {
        try {
            // Intentar borrar en la nube primero
            apiService.deleteLaboratorio(laboratorio.id)
        } catch (e: Exception) {
            Log.e("SyncError", "No se pudo borrar lab en nube: ${e.message}")
        }
        // Borrar siempre en local
        auditDao.deleteLaboratorio(laboratorio)
    }

    fun getEquiposByLaboratorio(labId: String): Flow<List<AuditItem>> = auditDao.getEquiposByLaboratorio(labId)
    suspend fun insertEquipo(item: AuditItem) = auditDao.insertEquipo(item)
    suspend fun updateEquipo(item: AuditItem) = auditDao.updateEquipo(item)
    
    // --- BORRADO BIDIRECCIONAL EQUIPO ---
    suspend fun deleteEquipo(item: AuditItem) {
        try {
            // Intentar borrar en la nube primero
            apiService.deleteEquipo(item.id)
        } catch (e: Exception) {
            Log.e("SyncError", "No se pudo borrar equipo en nube: ${e.message}")
        }
        // Borrar siempre en local
        auditDao.deleteEquipo(item)
    }

    suspend fun syncWithCloud(): Result<Unit> {
        return try {
            val responseLabs = apiService.getLaboratorios()
            if (responseLabs.isSuccessful) {
                responseLabs.body()?.forEach { auditDao.insertLaboratorio(it) }
            }

            val responseEquipos = apiService.getEquipos()
            if (responseEquipos.isSuccessful) {
                responseEquipos.body()?.forEach { equipo ->
                    equipo.isSynced = true
                    auditDao.insertEquipo(equipo)
                }
            }

            val labsLocales = auditDao.getAllLaboratoriosStatic()
            val equiposLocales = auditDao.getAllEquiposStatic()

            for (lab in labsLocales) {
                val res = apiService.syncLaboratorioIndividual(lab)
                if (res.isSuccessful) {
                    val labCloud = res.body()
                    if (labCloud != null && labCloud.id != lab.id) {
                        auditDao.updateEquiposLabId(lab.id, labCloud.id)
                        auditDao.deleteLaboratorio(lab)
                        auditDao.insertLaboratorio(labCloud)
                    }
                }
            }
            
            for (equipo in equiposLocales.filter { !it.isSynced }) {
                val res = apiService.syncEquipoIndividual(equipo)
                if (res.isSuccessful) {
                    val equipoCloud = res.body()
                    if (equipoCloud != null) {
                        auditDao.deleteEquipo(equipo)
                        equipoCloud.isSynced = true
                        auditDao.insertEquipo(equipoCloud)
                    }
                }
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("SyncError", "Detalle: ${e.message}")
            Result.failure(e)
        }
    }
}
