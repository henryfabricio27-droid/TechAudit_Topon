package com.example.techaudit

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Database
import com.example.techaudit.adapter.AuditAdapter
import com.example.techaudit.data.AuditDatabase
import com.example.techaudit.databinding.ActivityMainBinding
import com.example.techaudit.model.AuditItem
import com.example.techaudit.model.AuditStatus
import java.util.UUID

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: AuditAdapter

    private lateinit var database: AuditDatabase

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

       database = (application as TechAuditApp).database

        septupRecyclerView()

        cargardatosdebasededatos()
        binding.fabAgregar.setOnClickListener {
            insertarRegistro()
        }

        // 5. Configurar el RecyclerView
        setupRecyclerView(datosMock)
    }

    private fun setupRecyclerView() {
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
