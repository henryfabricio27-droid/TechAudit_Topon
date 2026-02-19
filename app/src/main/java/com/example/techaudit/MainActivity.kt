package com.example.techaudit

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.techaudit.adapter.AuditAdapter
import com.example.techaudit.databinding.ActivityMainBinding
import com.example.techaudit.model.AuditItem
import com.example.techaudit.model.AuditStatus
import java.util.UUID

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Habilitar EdgeToEdge ANTES de setContentView
        enableEdgeToEdge()

        // 2. Inflar View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 3. Configurar Insets (Barra de estado y navegación)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 4. Preparar datos de prueba (Mock)
        val datosMock = listOf(
            AuditItem(UUID.randomUUID().toString(), "Laptop HP ProBook", "lab 1 - Puesto 3 ", "26-01-23", AuditStatus.PENDIENTE),
            AuditItem(UUID.randomUUID().toString(), "Proyector HP", "laboratorio 23 ", "26-01-23", AuditStatus.OPERATIVO),
            AuditItem(UUID.randomUUID().toString(), "Impresora Cannon", "lab 2 -Puesto 6 ", "26-01-23", AuditStatus.DANIADO),
            AuditItem(UUID.randomUUID().toString(), "Impresora HP", "lab 3 -Puesto 9 ", "26-01-23", AuditStatus.PENDIENTE),
            AuditItem(UUID.randomUUID().toString(), "Scaner Lenovo", "lab 5 - Puesto 5 ", "26-01-23", AuditStatus.NO_ENCONTRADO)
        )

        // 5. Configurar el RecyclerView
        setupRecyclerView(datosMock)
    }

    private fun setupRecyclerView(lista: List<AuditItem>) {
        // Inicializar el adaptador usando la lista recibida por parámetro
        val adapter = AuditAdapter(lista) { itemSeleccionado ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("Extra_Item", itemSeleccionado)
            startActivity(intent)
        }
        
        binding.rvAuditoria.adapter = adapter
        binding.rvAuditoria.layoutManager = LinearLayoutManager(this)
    }
}
