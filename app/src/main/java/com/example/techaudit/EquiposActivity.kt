package com.example.techaudit

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techaudit.adapter.AuditAdapter
import com.example.techaudit.databinding.ActivityEquiposBinding
import com.example.techaudit.ui.AuditViewModel

class EquiposActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEquiposBinding
    private val viewModel: AuditViewModel by viewModels()
    private lateinit var adapter: AuditAdapter
    private var labId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEquiposBinding.inflate(layoutInflater)
        setContentView(binding.root)

        labId = intent.getStringExtra("LAB_ID") ?: ""
        val labNombre = intent.getStringExtra("LAB_NOMBRE") ?: "Equipos"
        binding.tvHeaderEquipos.text = labNombre

        setupRecyclerView()
        observeViewModel()
        setupSwipeToDelete()

        binding.fabAddEquipo.setOnClickListener {
            val intent = Intent(this, AddEditActivity::class.java)
            intent.putExtra("LAB_ID", labId)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        adapter = AuditAdapter(mutableListOf()) { equipo ->
            val intent = Intent(this, AddEditActivity::class.java)
            intent.putExtra("Extra_Item_Editar", equipo)
            intent.putExtra("LAB_ID", labId)
            startActivity(intent)
        }
        binding.rvEquipos.layoutManager = LinearLayoutManager(this)
        binding.rvEquipos.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.getEquiposByLaboratorio(labId).observe(this) { equipos ->
            adapter.actualizarLista(equipos)
        }
    }

    private fun setupSwipeToDelete() {
        val swipeHandler = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(r: RecyclerView, vh: RecyclerView.ViewHolder, t: RecyclerView.ViewHolder): Boolean = false
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val equipo = adapter.listAuditoria[position]
                viewModel.deleteEquipo(equipo)
            }
        }
        ItemTouchHelper(swipeHandler).attachToRecyclerView(binding.rvEquipos)
    }
}
