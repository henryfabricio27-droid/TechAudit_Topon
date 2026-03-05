package com.example.techaudit

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.techaudit.adapter.LaboratorioAdapter
import com.example.techaudit.databinding.ActivityMainBinding
import com.example.techaudit.databinding.DialogAddLaboratorioBinding
import com.example.techaudit.model.Laboratorio
import com.example.techaudit.ui.AuditViewModel
import java.util.UUID

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: AuditViewModel by viewModels()
    private lateinit var adapter: LaboratorioAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerView()
        observeViewModel()

        binding.fabAddLab.setOnClickListener {
            showAddLabDialog()
        }

        binding.btnSync.setOnClickListener {
            binding.pbSync.visibility = View.VISIBLE
            binding.btnSync.isEnabled = false
            viewModel.syncData()
        }
    }

    private fun setupRecyclerView() {
        adapter = LaboratorioAdapter(
            listLabs = emptyList(),
            onItemClick = { lab ->
                val intent = Intent(this, EquiposActivity::class.java)
                intent.putExtra("LAB_ID", lab.id)
                intent.putExtra("LAB_NOMBRE", lab.nombre)
                startActivity(intent)
            },
            onItemLongClick = { lab ->
                showDeleteConfirmDialog(lab)
            }
        )
        binding.rvLaboratorios.layoutManager = LinearLayoutManager(this)
        binding.rvLaboratorios.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.allLaboratorios.observe(this) { labs ->
            adapter.updateList(labs)
        }

        viewModel.syncStatus.observe(this) { result ->
            binding.pbSync.visibility = View.GONE
            binding.btnSync.isEnabled = true
            
            result.onSuccess {
                Toast.makeText(this, "Datos sincronizados", Toast.LENGTH_SHORT).show()
            }.onFailure {
                Toast.makeText(this, "Error de conexión: ${it.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showAddLabDialog() {
        val dialogBinding = DialogAddLaboratorioBinding.inflate(LayoutInflater.from(this))
        AlertDialog.Builder(this)
            .setTitle("Nuevo Laboratorio")
            .setView(dialogBinding.root)
            .setPositiveButton("Guardar") { _, _ ->
                val nombre = dialogBinding.etNombreLab.text.toString()
                val edificio = dialogBinding.etEdificioLab.text.toString()

                if (nombre.isNotBlank()) {
                    // Uso de UUID para evitar IDs duplicados y simplificar el código
                    val nuevoLab = Laboratorio(
                        id = UUID.randomUUID().toString(),
                        nombre = nombre,
                        edificio = edificio
                    )
                    viewModel.insertLaboratorio(nuevoLab)
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun showDeleteConfirmDialog(lab: Laboratorio) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar")
            .setMessage("¿Eliminar ${lab.nombre}? Esto borrará también todos sus equipos.")
            .setPositiveButton("Sí") { _, _ -> viewModel.deleteLaboratorio(lab) }
            .setNegativeButton("No", null)
            .show()
    }
}
