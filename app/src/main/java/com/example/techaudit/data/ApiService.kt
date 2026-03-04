package com.example.techaudit.data

import com.example.techaudit.model.AuditItem
import com.example.techaudit.model.Laboratorio
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    // Sincronizar laboratorios
    @POST("laboratorios")
    suspend fun syncLaboratorios(@Body laboratorios: List<Laboratorio>): Response<Unit>

    // Sincronizar equipos
    @POST("equipos")
    suspend fun syncEquipos(@Body equipos: List<AuditItem>): Response<Unit>

    companion object {
        // URL de MockAPI
        private const val BASE_URL = "https://69a8a3c037caab4b8c62370b.mockapi.io/api/v1/"

        fun create(): ApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}
