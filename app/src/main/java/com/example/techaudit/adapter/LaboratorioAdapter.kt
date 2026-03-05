package com.example.techaudit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.techaudit.databinding.ItemLaboratorioBinding
import com.example.techaudit.model.Laboratorio

class LaboratorioAdapter(
    private var listLabs: List<Laboratorio>,
    private val onItemClick: (Laboratorio) -> Unit,
    private val onItemLongClick: (Laboratorio) -> Unit
) : RecyclerView.Adapter<LaboratorioAdapter.LaboratorioViewHolder>() {

    inner class LaboratorioViewHolder(val binding: ItemLaboratorioBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaboratorioViewHolder {
        val binding = ItemLaboratorioBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return LaboratorioViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LaboratorioViewHolder, position: Int) {
        val lab = listLabs[position]
        holder.binding.tvNombreLab.text = lab.nombre
        holder.binding.tvEdificioLab.text = lab.edificio

        holder.itemView.setOnClickListener { onItemClick(lab) }
        holder.itemView.setOnLongClickListener {
            onItemLongClick(lab)
            true
        }
    }

    override fun getItemCount(): Int = listLabs.size

    fun updateList(newList: List<Laboratorio>) {
        listLabs = newList
        notifyDataSetChanged()
    }
}
