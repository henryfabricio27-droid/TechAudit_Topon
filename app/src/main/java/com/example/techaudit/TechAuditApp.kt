package com.example.techaudit

import android.app.Application
import com.example.techaudit.data.AuditDatabase

class TechAuditApp : Application() {
    val database: AuditDatabase by lazy { AuditDatabase.getDatabase(this) }
}
