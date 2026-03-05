package com.example.techaudit.ui

import android.app.Application
import androidx.lifecycle.*
import com.example.techaudit.TechAuditApp
import com.example.techaudit.data.AuditRepository
import com.example.techaudit.model.AuditItem
import com.example.techaudit.model.Laboratorio
import kotlinx.coroutines.launch

class AuditViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AuditRepository = (application as TechAuditApp).repository

    // --- LABORATORIOS ---
    val allLaboratorios: LiveData<List<Laboratorio>> = repository.allLaboratorios.asLiveData()

    fun insertLaboratorio(laboratorio: Laboratorio) = viewModelScope.launch {
        repository.insertLaboratorio(laboratorio)
    }

    fun deleteLaboratorio(laboratorio: Laboratorio) = viewModelScope.launch {
        repository.deleteLaboratorio(laboratorio)
    }

    // --- EQUIPOS ---
    fun getEquiposByLaboratorio(labId: String): LiveData<List<AuditItem>> {
        return repository.getEquiposByLaboratorio(labId).asLiveData()
    }

    fun insertEquipo(item: AuditItem) = viewModelScope.launch {
        repository.insertEquipo(item)
    }

    fun updateEquipo(item: AuditItem) = viewModelScope.launch {
        repository.updateEquipo(item)
    }

    fun deleteEquipo(item: AuditItem) = viewModelScope.launch {
        repository.deleteEquipo(item)
    }

    // --- SINCRONIZACIÓN ---
    private val _syncStatus = MutableLiveData<Result<Unit>>()
    val syncStatus: LiveData<Result<Unit>> = _syncStatus

    fun syncData() = viewModelScope.launch {
        val result = repository.syncWithCloud()
        _syncStatus.postValue(result)
    }
}
