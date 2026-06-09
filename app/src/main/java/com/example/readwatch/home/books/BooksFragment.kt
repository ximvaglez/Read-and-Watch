package com.example.readwatch.home.books

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.readwatch.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.readwatch.core.FragmentCommunicator
import com.example.readwatch.core.ResponseService
import com.example.readwatch.databinding.FragmentBooksBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import androidx.navigation.fragment.findNavController
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.example.readwatch.home.savedBook.LibraryAdapter

class BooksFragment : Fragment() {

    private var _binding: FragmentBooksBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<BooksViewModel>()
    private lateinit var communicator: FragmentCommunicator

    // Adapter de búsqueda (lista)
    private val searchAdapter = BooksAdapter { book ->
        val bundle = Bundle().apply { putParcelable("book", book) }
        findNavController().navigate(R.id.action_BooksFragment_to_bookDetailFragment, bundle)
    }

    // Adapter de biblioteca (grid)
    private val libraryAdapter = LibraryAdapter { savedBook ->
        val bundle = Bundle().apply { putParcelable("savedBook", savedBook) }
        findNavController().navigate(R.id.action_BooksFragment_to_savedBookDetailFragment, bundle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBooksBinding.inflate(inflater, container, false)
        communicator = requireActivity() as FragmentCommunicator

        // Lista de búsqueda (linear)
        binding.rvBooks.layoutManager = LinearLayoutManager(requireContext())
        binding.rvBooks.adapter = searchAdapter

        // Grid de biblioteca (2 columnas)
        binding.rvLibrary.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvLibrary.adapter = libraryAdapter

        binding.searchView.setupWithSearchBar(binding.searchBar)

        binding.searchView.editText.setOnEditorActionListener { _, _, _ ->
            val query = binding.searchView.text.toString()
            if (query.isNotBlank()) {
                binding.searchBar.setText(query)
                binding.searchView.hide()
                showSearchResults()
                viewModel.loadBooks(query)
            }
            false
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    when {
                        binding.searchView.isShowing -> binding.searchView.hide()
                        binding.rvBooks.isVisible -> showLibrary()
                        else -> {
                            isEnabled = false
                            requireActivity().onBackPressedDispatcher.onBackPressed()
                        }
                    }
                }
            }
        )

        // Carga la biblioteca al entrar
        viewModel.loadLibrary()
        observeState()
        return binding.root
    }

    private fun showSearchResults() {
        binding.rvBooks.visibility = View.VISIBLE
        binding.layoutLibrary.visibility = View.GONE
    }

    private fun showLibrary() {
        binding.rvBooks.visibility = View.GONE
        binding.layoutLibrary.visibility = View.VISIBLE
        binding.searchBar.setText("")
        searchAdapter.submitList(emptyList())
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                // Observa resultados de búsqueda
                launch {
                    viewModel.bookState.collect { state ->
                        when (state) {
                            ResponseService.Loading -> communicator.manageLoader(true)
                            is ResponseService.Success -> {
                                communicator.manageLoader(false)
                                searchAdapter.submitList(state.value)
                            }
                            is ResponseService.Error -> {
                                communicator.manageLoader(false)
                                Snackbar.make(binding.root, state.error, Snackbar.LENGTH_LONG).show()
                            }
                            null -> {}
                        }
                    }
                }

                // Observa la biblioteca guardada
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