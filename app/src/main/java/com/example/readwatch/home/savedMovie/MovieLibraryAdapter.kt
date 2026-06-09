package com.example.readwatch.home.savedMovie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.readwatch.core.model.SavedMovie
import com.example.readwatch.databinding.ItemSavedMovieBinding

class MovieLibraryAdapter(
    private val onItemClick: (SavedMovie) -> Unit
) : ListAdapter<SavedMovie, MovieLibraryAdapter.MovieLibraryViewHolder>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieLibraryViewHolder {
        val binding = ItemSavedMovieBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MovieLibraryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieLibraryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MovieLibraryViewHolder(
        private val binding: ItemSavedMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: SavedMovie) {
            binding.tvTitle.text = movie.title

            Glide.with(binding.ivPoster)
                .load(movie.poster)
                .centerCrop()
                .into(binding.ivPoster)

            binding.root.setOnClickListener { onItemClick(movie) }
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<SavedMovie>() {
            override fun areItemsTheSame(a: SavedMovie, b: SavedMovie) =
                a.imdbID == b.imdbID
            override fun areContentsTheSame(a: SavedMovie, b: SavedMovie) =
                a == b
        }
    }
}