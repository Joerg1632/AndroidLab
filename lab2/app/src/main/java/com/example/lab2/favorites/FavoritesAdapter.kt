package com.example.lab2.favorites

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lab2.R
import timber.log.Timber

class FavoritesAdapter(
    private var items: List<String>,
    private val onRemoveClick: (String) -> Unit
) : RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>() {

    inner class FavoritesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvCurrencyCode: TextView = view.findViewById(R.id.tvCurrencyCode)
        val btnRemove: ImageButton = view.findViewById(R.id.btnRemove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favorite, parent, false)
        return FavoritesViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        val code = items[position]
        holder.tvCurrencyCode.text = code
        holder.btnRemove.setOnClickListener {
            Timber.d("Clicked remove button for $code")
            onRemoveClick(code)
        }
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newItems: List<String>) {
        items = newItems
        notifyDataSetChanged()
    }
}
