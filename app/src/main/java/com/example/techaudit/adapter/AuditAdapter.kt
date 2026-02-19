package com.example.techaudit.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.techaudit.databinding.ItemAuditBinding
import com.example.techaudit.model.AuditItem
import com.example.techaudit.model.AuditStatus

//nos permite comunicar nuestro modelo con la vista
class AuditAdapter(
    private val listAuditoria: List<AuditItem>, // lista de auditores
    private val onItemClick: (AuditItem) -> Unit // funcion lambda para seleccionar
) : RecyclerView.Adapter<AuditAdapter.AuditViewHolder>() {

    inner class AuditViewHolder(val binding: ItemAuditBinding) : RecyclerView.ViewHolder(binding.root)

    // crear moldes
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuditViewHolder {
        val binding = ItemAuditBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AuditViewHolder(binding)
    }

    // cuantos datos tengo
    override fun getItemCount(): Int = listAuditoria.size

    //pintar los datos
    override fun onBindViewHolder(holder: AuditViewHolder, position: Int) {
        val auditItem = listAuditoria[position]

        holder.binding.tvNombreEquipo.text = auditItem.nombre
        holder.binding.tvUbicacion.text = auditItem.ubicacion
        holder.binding.tvEstadoLabel.text = auditItem.estado.name

        //logica visual
        val colorEstado = when (auditItem.estado) {
            AuditStatus.PENDIENTE -> Color.parseColor("#FFC107")
            AuditStatus.OPERATIVO -> Color.parseColor("#4CAF50")
            AuditStatus.DANIADO -> Color.parseColor("#F44336")
            AuditStatus.NO_ENCONTRADO -> Color.BLACK
        }

        //pintas la barra lateral
        holder.binding.viewStatusColor.setBackgroundColor(colorEstado)
        holder.binding.tvEstadoLabel.setTextColor(colorEstado)

        //configurar el clic en toda la tarjeta
        holder.itemView.setOnClickListener {
            onItemClick(auditItem)
        }
    }
}
