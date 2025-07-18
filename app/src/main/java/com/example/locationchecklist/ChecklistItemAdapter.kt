package com.example.locationchecklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChecklistItemAdapter(
    private val items: MutableList<ChecklistItem>,
    private val onListUpdated: () -> Unit // Called after delete or check toggle to save
) : RecyclerView.Adapter<ChecklistItemAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
        val itemText: TextView = itemView.findViewById(R.id.itemText)
        val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.checklist_item_row, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.itemText.text = item.text

        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.isChecked = item.isDone

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            item.isDone = isChecked
            onListUpdated()
        }

        holder.deleteButton.setOnClickListener {
            val pos = holder.adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                items.removeAt(pos)
                notifyItemRemoved(pos)
                onListUpdated()
            }
        }
    }

    override fun getItemCount() = items.size
}
