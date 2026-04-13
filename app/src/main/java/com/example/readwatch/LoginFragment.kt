package com.example.readwatch

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.example.readwatch.databinding.FragmentLoginBinding
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? =
        null //? es opcional, esa variable puede existir o no en memoria
    private val binding get() = _binding!!

    //binding: puente de enlace para acceder a los elementos de la pantalla
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        setupValidation()
        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.registerFragment)
        }
        return binding.root
    }

    //función para validar los datos
    private fun setupValidation() {
        binding.btnLogin.isEnabled = false
        binding.etEmail.addTextChangedListener {
            validateFields()
        }
        binding.etPassword.addTextChangedListener {
            validateFields()
        }
    }

    private fun validateFields() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        val isEmailValid = isValidEmail(email)
        val isPasswordValid = password.length >= 8

        binding.tilEmail.error = if (email.isNotEmpty() || isEmailValid) null else "Correo invalido"

        binding.tilPassword.error =
            if (password.isEmpty() || isPasswordValid) null else "Mínimo 8 caracteres"

        binding.btnLogin.isEnabled = email.isNotEmpty() && password.isNotEmpty() && isEmailValid && isPasswordValid
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}



