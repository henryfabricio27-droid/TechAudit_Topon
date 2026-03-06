package com.example.techaudit.data

import android.util.Log
import androidx.lifecycle.asLiveData
import androidx.lifecycle.asFlow
import com.example.techaudit.model.AuditItem
import com.example.techaudit.model.Laboratorio
import kotlinx.coroutines.flow.Flow

class AuditRepository(
    private val auditDao: AuditDAO,
    private val apiService: ApiService
) {

    // CORREGIDO: Llamada correcta a la función del DAO
    val allLaboratorios: Flow<List<Laboratorio>> = auditDao.getAllLaboratorios()

    suspend fun insertLaboratorio(laboratorio: Laboratorio) = auditDao.insertLaboratorio(laboratorio)
    
    suspend fun deleteLaboratorio(laboratorio: Laboratorio) {
        try {
            apiService.deleteLaboratorio(laboratorio.id)
        } catch (e: Exception) {
            Log.e("SyncError", "No se pudo borrar lab en nube: ${e.message}")
        }
        auditDao.deleteLaboratorio(laboratorio)
    }

    fun getEquiposByLaboratorio(labId: String): Flow<List<AuditItem>> = auditDao.getEquiposByLaboratorio(labId)
    
    suspend fun insertEquipo(item: AuditItem) {
        item.isSynced = false
        auditDao.insertEquipo(item)
    }
    
    suspend fun updateEquipo(item: AuditItem) {
        item.isSynced = false
        auditDao.updateEquipo(item)
    }
    
    suspend fun deleteEquipo(item: AuditItem) {
        try {
            apiService.deleteEquipo(item.id)
        } catch (e: Exception) {
            Log.e("SyncError", "No se pudo borrar equipo en nube: ${e.message}")
        }
        auditDao.deleteEquipo(item)
    }

    suspend fun syncWithCloud(): Result<Unit> {
        return try {
            // --- PASO 1: SUBIR CAMBIOS LOCALES PRIMERO (Para no perder ediciones) ---
            val labsLocales = auditDao.getAllLaboratoriosStatic()
            for (lab in labsLocales) {
                val res = apiService.updateLaboratorio(lab.id, lab)
                if (!res.isSuccessful) {
                    apiService.createLaboratorio(lab)
                }
            }
            
            val equiposLocales = auditDao.getAllEquiposStatic()
            for (equipo in equiposLocales.filter { !it.isSynced }) {
                val res = apiService.updateEquipo(equipo.id, equipo)
                if (res.isSuccessful || res.code() == 404) {
                    if (res.code() == 404) {
                        val createRes = apiService.createEquipo(equipo)
                        if (createRes.isSuccessful) {
                            val equipoCloud = createRes.body()
                            if (equipoCloud != null) {
                                auditDao.deleteEquipo(equipo)
                                equipoCloud.isSynced = true
                                auditDao.insertEquipo(equipoCloud)
                            }
                        }
                    } else {
                        equipo.isSynced = true
                        auditDao.updateEquipo(equipo)
                    }
                }
            }

            // --- PASO 2: DESCARGAR CAMBIOS DE LA NUBE ---
            val responseLabs = apiService.getLaboratorios()
            if (responseLabs.isSuccessful) {
                responseLabs.body()?.forEach { lab ->
                    auditDao.insertLaboratorio(lab)
                }
            }

            val responseEquipos = apiService.getEquipos()
            if (responseEquipos.isSuccessful) {
                responseEquipos.body()?.forEach { equipo ->
                    val local = equiposLocales.find { it.id == equipo.id }
                    if (local == null || local.isSynced) {
                        equipo.isSynced = true
                        auditDao.insertEquipo(equipo)
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
