package com.example.readwatch.home.savedMovie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.readwatch.core.ResponseService
import com.example.readwatch.core.model.SavedMovie
import com.example.readwatch.core.repositories.UserRepository
import com.example.readwatch.databinding.FragmentSavedMovieDetailBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class SavedMovieDetailFragment : Fragment() {

    private var _binding: FragmentSavedMovieDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var savedMovie: SavedMovie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedMovie = requireArguments().getParcelable("savedMovie")
            ?: error("SavedMovie requerido")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }

        binding.tvTitle.text = savedMovie.title
        binding.tvYear.text  = savedMovie.year
        binding.ratingBar.rating = savedMovie.rating

        Glide.with(binding.ivPoster)
            .load(savedMovie.poster)
            .into(binding.ivPoster)

        binding.btnSaveRating.setOnClickListener {
            val uid = FirebaseAuth.getInstance().currentUser?.uid
                ?: return@setOnClickListener
            val rating = binding.ratingBar.rating

            viewLifecycleOwner.lifecycleScope.launch {
                val result = UserRepository().rateMovie(uid, savedMovie.imdbID, rating)
                when (result) {
                    is ResponseService.Success ->
                        Snackbar.make(binding.root, "⭐ Calificación guardada",
                            Snackbar.LENGTH_SHORT).show()
                    is ResponseService.Error ->
                        Snackbar.make(binding.root, result.error,
                            Snackbar.LENGTH_SHORT).show()
                    else -> Unit
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}