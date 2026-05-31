package com.example.readwatch.home.books

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.readwatch.core.model.BookItem
import com.example.readwatch.databinding.ItemBookBinding

class BooksAdapter(
    private val onItemClick: (BookItem) -> Unit = {}
) : ListAdapter<BookItem, BooksAdapter.BookViewHolder>(DIFF) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookViewHolder {

        val binding = ItemBookBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: BookViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    inner class BookViewHolder(
        private val binding: ItemBookBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(book: BookItem) {

            binding.tvTitle.text =
                book.volumeInfo.title

            binding.tvAuthor.text =
                book.volumeInfo.authors?.joinToString(", ")
                    ?: "Autor desconocido"

            val imageUrl = book.volumeInfo.imageLinks?.thumbnail
                ?.replace("http://", "https://")

            Glide.with(binding.ivCover)
                .load(imageUrl)
                .centerCrop()
                .into(binding.ivCover)

            binding.root.setOnClickListener {
                onItemClick(book)
            }
        }
    }

    companion object {

        private val DIFF =
            object : DiffUtil.ItemCallback<BookItem>() {

                override fun areItemsTheSame(
                    oldItem: BookItem,
                    newItem: BookItem
                ): Boolean {

                    return oldItem.volumeInfo.title ==
                            newItem.volumeInfo.title
                }

                override fun areContentsTheSame(
                    oldItem: BookItem,
                    newItem: BookItem
                ): Boolean {

                    return oldItem == newItem
                }
            }
    }
}