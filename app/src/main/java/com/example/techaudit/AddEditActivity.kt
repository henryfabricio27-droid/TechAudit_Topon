package com.example.techaudit

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.techaudit.databinding.ActivityAddEditBinding
import com.example.techaudit.model.AuditItem
import com.example.techaudit.model.AuditStatus
import com.example.techaudit.ui.AuditViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class AddEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditBinding
    private val viewModel: AuditViewModel by viewModels()
    private var itemEditar: AuditItem? = null
    private var labId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // RECUPERACIÓN SEGURA DEL LAB_ID
        labId = intent.getStringExtra("LAB_ID") ?: ""
        
        // Recuperar el item si viene de edición
        itemEditar = if (android.os.Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("Extra_Item_Editar", AuditItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("Extra_Item_Editar")
        }

        // Si estamos editando, el labId debe ser el del objeto original
        itemEditar?.let { labId = it.laboratorioId }

        setupSpinner()

        if (itemEditar != null) {
            binding.tvTitle.text = "Editar Equipo"
            binding.etNombre.setText(itemEditar!!.nombre)
            binding.etNotas.setText(itemEditar!!.notas)
            val pos = AuditStatus.entries.indexOf(itemEditar!!.estado)
            binding.spEstado.setSelection(pos)
        } else {
            binding.tvTitle.text = "Nuevo Equipo"
        }

        binding.btnGuardar.setOnClickListener {
            guardarRegistro()
        }
    }

    private fun setupSpinner() {
        val estados = AuditStatus.entries.toTypedArray()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, estados)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spEstado.adapter = adapter
    }

    private fun guardarRegistro() {
        val nombre = binding.etNombre.text.toString()
        val notas = binding.etNotas.text.toString()
        val estado = binding.spEstado.selectedItem as AuditStatus

        if (nombre.isBlank() || labId.isBlank()) {
            Toast.makeText(this, "Error: Nombre o Laboratorio no válidos", Toast.LENGTH_SHORT).show()
            return
        }

        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val fecha = sdf.format(Date())

        if (itemEditar == null) {
            // NUEVO REGISTRO
            val nuevo = AuditItem(
                id = UUID.randomUUID().toString(),
                nombre = nombre,
                laboratorioId = labId, // Aquí se asegura el vínculo
                fechaRegistro = fecha,
                estado = estado,
                notas = notas
            )
            viewModel.insertEquipo(nuevo)
        } else {
            // ACTUALIZAR REGISTRO
            val actualizado = itemEditar!!.copy(
                nombre = nombre,
                estado = estado,
                notas = notas
            )
            viewModel.updateEquipo(actualizado)
        }

        Toast.makeText(this, "Guardado en la sala", Toast.LENGTH_SHORT).show()
        finish()
    }
}
