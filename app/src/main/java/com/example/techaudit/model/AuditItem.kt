package com.example.techaudit.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

enum class AuditStatus {
    PENDIENTE,
    OPERATIVO,
    DANIADO,
    NO_ENCONTRADO
}

@Parcelize
data class AuditItem(
    val id: String,
    val nombre: String,
    val ubicacion: String,
    val fechaRegistro: String,
    var estado: AuditStatus = AuditStatus.PENDIENTE,
    var notas: String = "",
    var fotoUri: String ?= null
) : Parcelable
