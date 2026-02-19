package com.example.techaudit

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.techaudit.databinding.ActivityDetailBinding
import com.example.techaudit.model.AuditItem
import com.example.techaudit.model.AuditStatus

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Habilitar EdgeToEdge
        enableEdgeToEdge()

        // 2. Inflar View Binding
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 3. Ajustar padding para barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 4. Recuperar el objeto pasado por el Intent
        val item = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("Extra_Item", AuditItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<AuditItem>("Extra_Item")
        }

        // 5. Mostrar datos si el objeto existe
        item?.let {
            mostrarDetalles(it)
        }
    }

    private fun mostrarDetalles(item: AuditItem) {
        binding.tvDetalleNombre.text = item.nombre
        binding.tvDetalleId.text = "ID: ${item.id.substring(0, 8)}"
        binding.tvDetalleUbicacion.text = item.ubicacion
        binding.tvDetalleFecha.text = item.fechaRegistro
        
        // Manejo de notas vacias
        binding.tvDetalleNotas.text = if (item.notas.isEmpty()) "Sin notas registradas" else item.notas

        // Logica visual segun el estado
        val color = when (item.estado) {
            AuditStatus.PENDIENTE -> Color.parseColor("#FFC107")
            AuditStatus.OPERATIVO -> Color.parseColor("#4CAF50")
            AuditStatus.DANIADO -> Color.parseColor("#F44336")
            AuditStatus.NO_ENCONTRADO -> Color.BLACK
        }

        binding.viewHeaderStatus.setBackgroundColor(color)
        title = "Detalle: ${item.estado.name}"
    }
}
