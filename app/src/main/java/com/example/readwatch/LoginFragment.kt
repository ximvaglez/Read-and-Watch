package com.example.readwatch

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.readwatch.core.FragmentCommunicator
import com.example.readwatch.databinding.FragmentLoginBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.readwatch.core.ResponseService
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? =
        null //? es opcional, esa variable puede existir o no en memoria
    private val binding get() = _binding!!
    private val viewModel by viewModels<SignInViewModel>()

    private lateinit var communicator: FragmentCommunicator

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
        communicator = requireActivity() as FragmentCommunicator
        setupValidation()
        setUpClickListeners()
        observeState()
        return binding.root
    }

    //función para validar los datos
    private fun setupValidation() {
        binding.signInButton.isEnabled = false
        binding.emailTiet.addTextChangedListener {
            validateAndEnable()
        }
        binding.passwordTiet.addTextChangedListener {
            validateAndEnable()
        }
    }

    private fun validateAndEnable() {
        val email = binding.emailTiet.text.toString().trim()
        val password = binding.passwordTiet.text.toString().trim()

        binding.emailTil.error = viewModel.validateEmail(email)
        binding.passwordTil.error = viewModel.validatePassword(password)
        binding.signInButton.isEnabled = viewModel.isLoginFormValid(email, password)
       }

    private fun setUpClickListeners() {
        binding.signInButton.setOnClickListener {
            val email= binding.emailTiet.text.toString().trim()
            val password = binding.passwordTiet.text.toString().trim()
            viewModel.requestLogin(email, password)
        }
        binding.registerText.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun observeState(){
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.signInState.collect{
                    state -> when(state){
                        is ResponseService.Loading ->{
                            communicator.manageLoader(true)
                            binding.signInButton.isEnabled = false
                        }
                        is ResponseService.Success ->{
                            communicator.manageLoader(false)
                        }
                        is ResponseService.Error -> {
                            communicator.manageLoader(false)
                            binding.signInButton.isEnabled = true
                            Snackbar.make(binding.root, state.error, Snackbar.LENGTH_LONG).show()
                        }
                    null -> Unit
                    }
                }
            }
        }
    }
}



