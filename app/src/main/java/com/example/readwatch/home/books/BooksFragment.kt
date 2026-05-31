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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.readwatch.core.FragmentCommunicator
import com.example.readwatch.core.ResponseService
import com.example.readwatch.databinding.FragmentBooksBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class BooksFragment : Fragment() {

    private var _binding: FragmentBooksBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<BooksViewModel>()

    private lateinit var communicator: FragmentCommunicator

    private val adapter = BooksAdapter { book ->

        // Aquí después puedes abrir una pantalla de detalle
        // o guardar el libro en Firebase

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBooksBinding.inflate(inflater, container, false)
        communicator = requireActivity() as FragmentCommunicator

        binding.rvBooks.layoutManager = LinearLayoutManager(requireContext())
        binding.rvBooks.adapter = adapter

        // Conecta el SearchBar con la SearchView  y da la animación automática
        binding.searchView.setupWithSearchBar(binding.searchBar)

        // Para que el usuario escriba y tocque buscar
        binding.searchView.editText.setOnEditorActionListener { _, _, _ ->
            val query = binding.searchView.text.toString()
            if (query.isNotBlank()) {
                binding.searchBar.setText(query) // muestra lo buscado en la barra
                binding.searchView.hide()        // cierra el SearchView
                viewModel.loadBooks(query)       // busca los libros
            }
            false
        }

        observeState()
        return binding.root
    }

    private fun observeState() {

        viewLifecycleOwner.lifecycleScope.launch {

            repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {

                viewModel.bookState.collect { state ->

                    when (state) {

                        ResponseService.Loading -> {

                            communicator.manageLoader(true)
                        }

                        is ResponseService.Success -> {

                            communicator.manageLoader(false)

                            adapter.submitList(
                                state.value
                            )
                        }

                        is ResponseService.Error -> {

                            communicator.manageLoader(false)

                            Snackbar.make(
                                binding.root,
                                state.error,
                                Snackbar.LENGTH_LONG
                            ).show()
                        }

                        null -> {}
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