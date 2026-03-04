package com.example.techaudit.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.techaudit.model.AuditItem
import com.example.techaudit.model.Laboratorio

@Database(entities = [Laboratorio::class, AuditItem::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AuditDatabase : RoomDatabase() {

    abstract fun auditDao(): AuditDAO

    companion object {
        @Volatile
        private var INSTANCE: AuditDatabase? = null

        fun getDatabase(context: Context): AuditDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AuditDatabase::class.java,
                    "techaudit.db"
                )
                .fallbackToDestructiveMigration() // Para facilitar el desarrollo con cambios de esquema
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
