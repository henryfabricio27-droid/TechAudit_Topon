package com.example.techaudit.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

enum class AuditStatus {
    @SerializedName("PENDIENTE") PENDIENTE,
    @SerializedName("OPERATIVO") OPERATIVO,
    @SerializedName("DANIADO") DANIADO,
    @SerializedName("NO_ENCONTRADO") NO_ENCONTRADO
}

@Parcelize
@Entity(
    tableName = "equipos",
    foreignKeys = [
        ForeignKey(
            entity = Laboratorio::class,
            parentColumns = ["id"],
            childColumns = ["laboratorioId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["laboratorioId"])]
)
data class AuditItem(
    @PrimaryKey
    @SerializedName("id")
    val id: String,
    
    @SerializedName("nombre")
    val nombre: String,
    
    @SerializedName("laboratorioId")
    val laboratorioId: String,
    
    @SerializedName("fechaRegistro")
    val fechaRegistro: String,
    
    @SerializedName("estado")
    var estado: AuditStatus = AuditStatus.PENDIENTE,
    
    @SerializedName("notas")
    var notas: String = "",
    
    // Este campo es solo local, no se envía a la API
    var isSynced: Boolean = false
) : Parcelable
