package com.example.readwatch.home.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.readwatch.core.FragmentCommunicator
import com.example.readwatch.core.ResponseService
import com.example.readwatch.databinding.FragmentMoviesBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import com.example.readwatch.R
import androidx.navigation.fragment.findNavController
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.example.readwatch.home.savedMovie.MovieLibraryAdapter

class MoviesFragment : Fragment() {

    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<MoviesViewModel>()
    private lateinit var communicator: FragmentCommunicator

    private val searchAdapter = MoviesAdapter { movie ->
        val bundle = Bundle().apply { putParcelable("movie", movie) }
        findNavController().navigate(
            R.id.action_MoviesFragment_to_movieDetailFragment, bundle)
    }

    private val libraryAdapter = MovieLibraryAdapter { savedMovie ->
        val bundle = Bundle().apply { putParcelable("savedMovie", savedMovie) }
        findNavController().navigate(
            R.id.action_MoviesFragment_to_savedMovieDetailFragment, bundle
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        communicator = requireActivity() as FragmentCommunicator

        binding.rvMovies.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMovies.adapter = searchAdapter

        binding.rvMovieLibrary.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvMovieLibrary.adapter = libraryAdapter

        binding.searchView.setupWithSearchBar(binding.searchBar)

        binding.searchView.editText.setOnEditorActionListener { _, _, _ ->
            val query = binding.searchView.text.toString()
            if (query.isNotBlank()) {
                binding.searchBar.setText(query)
                binding.searchView.hide()
                showSearchResults()
                viewModel.searchMovies(query)
            }
            false
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    when {
                        binding.searchView.isShowing -> binding.searchView.hide()
                        binding.rvMovies.isVisible -> showLibrary()
                        else -> {
                            isEnabled = false
                            requireActivity().onBackPressedDispatcher.onBackPressed()
                        }
                    }
                }
            }
        )

        viewModel.loadLibrary()
        observeState()
        return binding.root
    }

    private fun showSearchResults() {
        binding.rvMovies.visibility = View.VISIBLE
        binding.layoutMovieLibrary.visibility = View.GONE
    }

    private fun showLibrary() {
        binding.rvMovies.visibility = View.GONE
        binding.layoutMovieLibrary.visibility = View.VISIBLE
        binding.searchBar.setText("")
        searchAdapter.submitList(emptyList())
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.movieState.collect { state ->
                        when (state) {
                            is ResponseService.Loading ->
                                communicator.manageLoader(true)
                            is ResponseService.Success -> {
                                communicator.manageLoader(false)
                                searchAdapter.submitList(state.value)
                            }
                            is ResponseService.Error -> {
                                communicator.manageLoader(false)
                                Snackbar.make(binding.root, state.error,
                                    Snackbar.LENGTH_LONG).show()
                            }
                            null -> {}
                        }
                    }
                }

                launch {
                    viewModel.libraryState.collect { state ->
                        if (state is ResponseService.Success) {
                            libraryAdapter.submitList(state.value)
                        }
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