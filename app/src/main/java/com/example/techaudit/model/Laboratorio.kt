package com.example.techaudit.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "laboratorios")
data class Laboratorio(
    @PrimaryKey
    @SerializedName("id")
    val id: String,
    
    @SerializedName("nombre")
    val nombre: String,
    
    @SerializedName("edificio")
    val edificio: String
) : Parcelable
