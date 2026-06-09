package com.example.readwatch.home.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readwatch.core.ResponseService
import com.example.readwatch.core.repositories.UserRepository
import com.example.readwatch.core.repositories.UserService
import com.example.readwatch.onboarding.personalInfo.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AccountViewModel(
    private val service: UserService = UserRepository()
) : ViewModel() {

    private val _profileState =
        MutableStateFlow<ResponseService<UserProfile>?>(null)
    val profileState: StateFlow<ResponseService<UserProfile>?> =
        _profileState.asStateFlow()

    fun loadProfile() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        viewModelScope.launch {
            _profileState.value = ResponseService.Loading
            _profileState.value = service.getUserProfile(uid)
        }
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
    }
}