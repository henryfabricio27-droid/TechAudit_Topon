package com.example.techaudit

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.techaudit.databinding.ActivityAddEditBinding
import com.example.techaudit.model.AuditItem
import com.example.techaudit.model.AuditStatus
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

class AddEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()

        // Cambiado binding.main por binding.root ya que el XML no tiene el ID 'main'
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        setupSpinner()

        binding.btnGuardar.setOnClickListener {
            guardarRegistro()
        }
    }

    private fun setupSpinner() {
        // Obtenemos los valores del Enum
        val estados = AuditStatus.values()

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            estados
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Usamos spEstado para que coincida con el ID del XML
        binding.spEstado.adapter = adapter
    }

    private fun guardarRegistro() {
        val nombre = binding.etNombre.text.toString()
        val ubicacion = binding.etUbicacion.text.toString()
        val notas = binding.etNotas.text.toString()

        if (nombre.isBlank() || ubicacion.isBlank()) {
            Toast.makeText(this, "Nombre y Ubicación son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        // Usamos spEstado para que coincida con el ID del XML
        val estadoSeleccionado = binding.spEstado.selectedItem as AuditStatus

        val nuevoItem = AuditItem(
            id = UUID.randomUUID().toString(),
            nombre = nombre,
            ubicacion = ubicacion,
            fechaRegistro = Date().toString(),
            estado = estadoSeleccionado,
            notas = notas
        )

        val database = (application as TechAuditApp).database

        lifecycleScope.launch {
            database.auditDao().insert(nuevoItem)
            Toast.makeText(this@AddEditActivity, "Guardado!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
