package com.example.techaudit.data

import com.example.techaudit.model.AuditItem
import com.example.techaudit.model.Laboratorio
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @GET("laboratorios")
    suspend fun getLaboratorios(): Response<List<Laboratorio>>

    @GET("equipos")
    suspend fun getEquipos(): Response<List<AuditItem>>

    @POST("laboratorios")
    suspend fun syncLaboratorioIndividual(@Body laboratorio: Laboratorio): Response<Laboratorio>

    @POST("equipos")
    suspend fun syncEquipoIndividual(@Body equipo: AuditItem): Response<AuditItem>

    @DELETE("laboratorios/{id}")
    suspend fun deleteLaboratorio(@Path("id") id: String): Response<Unit>

    @DELETE("equipos/{id}")
    suspend fun deleteEquipo(@Path("id") id: String): Response<Unit>

    companion object {
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
