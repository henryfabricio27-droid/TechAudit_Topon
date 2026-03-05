package com.example.techaudit

import android.app.Application
import com.example.techaudit.data.ApiService
import com.example.techaudit.data.AuditDatabase
import com.example.techaudit.data.AuditRepository

class TechAuditApp : Application() {
    
    val database: AuditDatabase by lazy { AuditDatabase.getDatabase(this) }
    
    val repository: AuditRepository by lazy {
        AuditRepository(database.auditDao(), ApiService.create())
    }
}
