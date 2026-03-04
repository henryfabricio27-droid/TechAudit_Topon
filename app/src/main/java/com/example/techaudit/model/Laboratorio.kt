package com.example.techaudit.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "laboratorios")
data class Laboratorio(
    @PrimaryKey
    val id: String,
    val nombre: String,
    val edificio: String
) : Parcelable
