package com.example.readwatch.core.repositories

import com.example.readwatch.core.ResponseService
import com.example.readwatch.onboarding.personalInfo.model.UserProfile

interface UserService {
    suspend fun saveUserInfo(userProfile: UserProfile): ResponseService<Unit>
}