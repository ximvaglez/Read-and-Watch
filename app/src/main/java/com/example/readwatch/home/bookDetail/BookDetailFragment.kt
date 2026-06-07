package com.example.readwatch.home.bookDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.readwatch.core.model.BookItem
import com.example.readwatch.databinding.FragmentBookDetailBinding

class BookDetailFragment : Fragment() {

    private var _binding: FragmentBookDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var book: BookItem
    private var expanded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        book = requireArguments().getParcelable("book")
            ?: error("Book requerido")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentBookDetailBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.tvTitle.text =
            book.volumeInfo.title ?: "Sin título"

        binding.tvAuthor.text =
            book.volumeInfo.authors?.joinToString(", ")
                ?: "Autor desconocido"

        val description = book.volumeInfo.description
            ?: "No hay descripción disponible"

        binding.tvDescription.text = description

        if (description == "No hay descripción disponible") {
            binding.tvToggleDescription.visibility = View.GONE
        }

        Glide.with(binding.ivBook)
            .load(
                book.volumeInfo.imageLinks?.thumbnail
                    ?.replace("http://", "https://")
            )
            .into(binding.ivBook)

        binding.tvToggleDescription.setOnClickListener {

            expanded = !expanded

            if (expanded) {
                binding.tvDescription.maxLines = Int.MAX_VALUE
                binding.tvDescription.ellipsize = null
                binding.tvToggleDescription.text = "Ver menos"
            } else {
                binding.tvDescription.maxLines = 7
                binding.tvDescription.ellipsize =
                    android.text.TextUtils.TruncateAt.END
                binding.tvToggleDescription.text = "Ver más"
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}