package com.example.readwatch.home.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.readwatch.core.model.MovieItem
import com.example.readwatch.databinding.ItemMovieBinding

class MoviesAdapter(
    private val onItemClick: (MovieItem) -> Unit = {}
) : ListAdapter<MovieItem, MoviesAdapter.MovieViewHolder>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MovieViewHolder(
        private val binding: ItemMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: MovieItem) {
            binding.tvTitle.text = movie.Title ?: "Sin título"
            binding.tvYear.text = movie.Year ?: ""

            Glide.with(binding.ivPoster)
                .load(movie.Poster)
                .centerCrop()
                .into(binding.ivPoster)

            binding.root.setOnClickListener {
                onItemClick(movie)
            }
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<MovieItem>() {
            override fun areItemsTheSame(oldItem: MovieItem, newItem: MovieItem) =
                oldItem.imdbID == newItem.imdbID

            override fun areContentsTheSame(oldItem: MovieItem, newItem: MovieItem) =
                oldItem == newItem
        }
    }
}