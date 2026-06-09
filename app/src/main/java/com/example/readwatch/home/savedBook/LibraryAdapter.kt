package com.example.readwatch.home.savedBook

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.readwatch.core.model.SavedBook
import com.example.readwatch.databinding.ItemSavedBookBinding

class LibraryAdapter(
    private val onItemClick: (SavedBook) -> Unit
) : ListAdapter<SavedBook, LibraryAdapter.LibraryViewHolder>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryViewHolder {
        val binding = ItemSavedBookBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return LibraryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LibraryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class LibraryViewHolder(
        private val binding: ItemSavedBookBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(book: SavedBook) {
            binding.tvTitle.text = book.title

            Glide.with(binding.ivCover)
                .load(book.thumbnail.replace("http://", "https://"))
                .centerCrop()
                .into(binding.ivCover)

            binding.root.setOnClickListener { onItemClick(book) }
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<SavedBook>() {
            override fun areItemsTheSame(a: SavedBook, b: SavedBook) =
                a.title == b.title
            override fun areContentsTheSame(a: SavedBook, b: SavedBook) =
                a == b
        }
    }
}