package com.example.techaudit.data

import com.example.techaudit.model.AuditItem
import kotlinx.coroutines.flow.Flow

class AuditRepository(private val auditDao: AuditDAO) {

    val allItems: Flow<List<AuditItem>> = auditDao.getAllItems()

    suspend fun insert(item: AuditItem) {
        auditDao.insert(item)
    }

    suspend fun update(item: AuditItem) {
        auditDao.update(item)
    }

    suspend fun delete(item: AuditItem) {
        auditDao.delete(item)
    }
}
