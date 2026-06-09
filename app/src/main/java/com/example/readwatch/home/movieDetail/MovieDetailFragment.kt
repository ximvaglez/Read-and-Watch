package com.example.readwatch.home.movieDetail


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.readwatch.core.FragmentCommunicator
import com.example.readwatch.core.ResponseService
import com.example.readwatch.core.model.MovieItem
import com.example.readwatch.core.repositories.UserRepository
import com.example.readwatch.databinding.FragmentMovieDetailBinding
import com.example.readwatch.home.movieDetail.MovieDetailViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class MovieDetailFragment : Fragment() {

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<MovieDetailViewModel>()
    private lateinit var communicator: FragmentCommunicator
    private lateinit var movie: MovieItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movie = requireArguments().getParcelable("movie")
            ?: error("Movie requerida")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        communicator = requireActivity() as FragmentCommunicator
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnFavorite.setOnClickListener {
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            if (uid == null) {
                Snackbar.make(binding.root, "Inicia sesión primero",
                    Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewLifecycleOwner.lifecycleScope.launch {
                val result = UserRepository().saveFavoriteMovie(uid, movie)
                when (result) {
                    is ResponseService.Success ->
                        Snackbar.make(binding.root, "🎬 Película guardada",
                            Snackbar.LENGTH_SHORT).show()
                    is ResponseService.Error ->
                        Snackbar.make(binding.root, result.error,
                            Snackbar.LENGTH_SHORT).show()
                    else -> Unit
                }
            }
        }

        // Carga el detalle completo usando el imdbID
        movie.imdbID?.let { viewModel.loadDetail(it) }

        observeState()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.detailState.collect { state ->
                    when (state) {
                        is ResponseService.Loading -> {
                            communicator.manageLoader(true)
                        }
                        is ResponseService.Success -> {
                            communicator.manageLoader(false)
                            val detail = state.value

                            binding.tvTitle.text = detail.Title ?: "Sin título"
                            binding.tvGenre.text = detail.Genre ?: ""
                            binding.tvInfo.text =
                                "${detail.Year ?: ""} · ${detail.Runtime ?: ""} · ⭐ ${detail.imdbRating ?: "N/A"}"
                            binding.tvDirector.text = detail.Director ?: "Desconocido"
                            binding.tvActors.text = detail.Actors ?: "Desconocidos"

                            val plot = detail.Plot ?: "Sin sinopsis disponible"
                            binding.tvPlot.text = plot

                            Glide.with(binding.ivPoster)
                                .load(detail.Poster)
                                .into(binding.ivPoster)

                        }
                        is ResponseService.Error -> {
                            communicator.manageLoader(false)
                        }
                        null -> Unit
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}