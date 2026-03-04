package com.example.techaudit.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.techaudit.data.AuditRepository
import com.example.techaudit.model.AuditItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.techaudit.TechAuditApp
import kotlinx.coroutines.launch

class AuditViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AuditRepository
    val allItems: LiveData<List<AuditItem>>

    init {
        val dao = (application as TechAuditApp).database.auditDao()
        repository = AuditRepository(dao)
        allItems = repository.allItems.asLiveData()
    }

    fun delete(item: AuditItem) = viewModelScope.launch {
        repository.delete(item)
    }
}