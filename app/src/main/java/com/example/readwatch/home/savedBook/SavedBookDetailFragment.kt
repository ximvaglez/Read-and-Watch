package com.example.readwatch.home.savedBook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.readwatch.core.ResponseService
import com.example.readwatch.core.model.SavedBook
import com.example.readwatch.core.repositories.UserRepository
import com.example.readwatch.databinding.FragmentSavedBookDetailBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class SavedBookDetailFragment : Fragment() {

    private var _binding: FragmentSavedBookDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var savedBook: SavedBook

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedBook = requireArguments().getParcelable("savedBook")
            ?: error("SavedBook requerido")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedBookDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }

        binding.tvTitle.text  = savedBook.title
        binding.tvAuthor.text = savedBook.author
        binding.ratingBar.rating = savedBook.rating

        Glide.with(binding.ivCover)
            .load(savedBook.thumbnail.replace("http://", "https://"))
            .into(binding.ivCover)

        binding.btnSaveRating.setOnClickListener {
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@setOnClickListener
            val rating = binding.ratingBar.rating

            viewLifecycleOwner.lifecycleScope.launch {
                val result = UserRepository().rateBook(uid, savedBook.title, rating)
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