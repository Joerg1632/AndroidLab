package com.example.lab2.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lab2.R
import timber.log.Timber

class CurrencyAdapter(
    private var items: List<String>,
    private val favorites: MutableSet<String>,
    private val onFavoriteClick: (String) -> Unit,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {

    inner class CurrencyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvCurrencyCode: TextView = view.findViewById(R.id.tvCurrencyCode)
        val ivFavorite: ImageView = view.findViewById(R.id.ivFavorite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_currency, parent, false)
        return CurrencyViewHolder(view)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val code = items[position]
        holder.tvCurrencyCode.text = code

        holder.ivFavorite.setImageResource(
            if (favorites.contains(code)) R.drawable.ic_star_filled
            else R.drawable.ic_star_outline
        )

        holder.ivFavorite.setOnClickListener {
            Timber.i("Favorite clicked: $code")
            onFavoriteClick(code)
        }

        holder.itemView.setOnClickListener {
            Timber.i("Item clicked: $code")
            onItemClick(code)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<String>, newFavorites: Set<String>) {
        items = newItems.sortedByDescending { newFavorites.contains(it) } // избранные сверху
        favorites.clear()
        favorites.addAll(newFavorites)
        Timber.d("Adapter data updated: ${items.size} items, favorites: $favorites")
        notifyDataSetChanged()
    }
}