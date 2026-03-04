package com.example.techaudit

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techaudit.adapter.AuditAdapter
import com.example.techaudit.data.AuditDatabase
import com.example.techaudit.databinding.ActivityMainBinding
import com.example.techaudit.model.AuditItem
import com.example.techaudit.model.AuditStatus
import com.example.techaudit.ui.AuditViewModel
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: AuditAdapter

    private val viewModel: AuditViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Variable para guardar el item a editar (Declaración corregida)
        var itemEditar: AuditItem? = null

        // DETECTAR MODO EDICION
        if (intent.hasExtra("Extra_Item_Editar")) {
            binding.fabAgregar.hide()

            // RECUPERAMOS EL OBJETO (Corregido: asignación y llaves)
            itemEditar = if (android.os.Build.VERSION.SDK_INT >= 33) {
                intent.getParcelableExtra("Extra_Item_Editar", AuditItem::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra("Extra_Item_Editar")
            }
        }

        // llenamos loa campos de texto (Corregido: llaves y posición)
        itemEditar?.let { item ->
            // Nota: etNombre, etUbicacion, etNotas y spEstado deben estar en activity_main.xml
            // binding.etNombre.setText(item.nombre)
            // binding.etUbicacion.setText(item.ubicacion)
            // binding.etNotas.setText(item.notas)

            // Seleccionar el Spinner Correcto
            val posicionSpinner = AuditStatus.values().indexOf(item.estado)
            // binding.spEstado.setSelection(posicionSpinner)
        }

        // El resto del código debe estar FUERA del bloque itemEditar?.let para que siempre se ejecute
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel.allItems.observe(this) { items ->
            adapter.actualizarLista(items)
        }
        setupRecyclerView()

        configurarDeslizarParaBorrar()

        binding.fabAgregar.setOnClickListener {

        }
    }

    private fun setupRecyclerView() {
        adapter = AuditAdapter(mutableListOf()) { itemSeleccionado ->
            val intent = Intent(this, AddEditActivity::class.java)
            intent.putExtra("Extra_Item_Editar", itemSeleccionado)
            startActivity(intent)
        }

        binding.rvAuditoria.adapter = adapter
        binding.rvAuditoria.layoutManager = LinearLayoutManager(this)
    }

    private fun configurarDeslizarParaBorrar() {
        val swipeHandler = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val posicion = viewHolder.adapterPosition
                val itemABorrar = adapter.listAuditoria[posicion]

               viewModel.delete(itemABorrar)
                Toast.makeText(this@MainActivity, "Equipo borrado", Toast.LENGTH_SHORT).show()
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.rvAuditoria)
    }


}
