package com.example.techaudit

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.techaudit.data.AuditDatabase
import com.example.techaudit.databinding.ActivityAddEditBinding
import com.example.techaudit.model.AuditItem
import com.example.techaudit.model.AuditStatus
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

class AddEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditBinding
    private var itemEditar: AuditItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Detectar MODO EDICION
        if (intent.hasExtra("EXTRA_ITEM_EDITAR")) {
            // Recuperamos el objeto
            itemEditar = if (android.os.Build.VERSION.SDK_INT >= 33) {
                intent.getParcelableExtra("EXTRA_ITEM_EDITAR", AuditItem::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra("EXTRA_ITEM_EDITAR")
            }
        }

        // Si existe, rellenar campos
        itemEditar?.let {
            binding.etNombre.setText(it.nombre)
            binding.etUbicacion.setText(it.ubicacion)
            binding.etNotas.setText(it.notas)
            val pos = AuditStatus.values().indexOf(it.estado)
            binding.spEstado.setSelection(pos)
        }
        
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupSpinner()

        binding.btnGuardar.setOnClickListener {
            guardaroActualizar()
        }
    }

    private fun setupSpinner() {
        val estados = AuditStatus.values()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, estados)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spEstado.adapter = adapter
    }

    private fun guardaroActualizar() {
        val nombre = binding.etNombre.text.toString()
        val ubicacion = binding.etUbicacion.text.toString()
        val notas = binding.etNotas.text.toString()

        if (nombre.isBlank() || ubicacion.isBlank()) {
            Toast.makeText(this, "Nombre y Ubicación son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        val estadoSeleccionado = binding.spEstado.selectedItem as AuditStatus
        val database = (application as TechAuditApp).database

        lifecycleScope.launch {
            if (itemEditar == null) {
                // MODO CREAR
                val nuevoItem = AuditItem(
                    id = UUID.randomUUID().toString(),
                    nombre = nombre,
                    ubicacion = ubicacion,
                    fechaRegistro = Date().toString(),
                    estado = estadoSeleccionado,
                    notas = notas
                )
                database.auditDao().insert(nuevoItem)
                Toast.makeText(this@AddEditActivity, "Equipo creado", Toast.LENGTH_SHORT).show()
            } else {
                // MODO EDITAR
                val itemActualizado = itemEditar!!.copy(
                    nombre = nombre,
                    ubicacion = ubicacion,
                    estado = estadoSeleccionado,
                    notas = notas
                )
                database.auditDao().update(itemActualizado)
                Toast.makeText(this@AddEditActivity, "Equipo actualizado", Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }
}
