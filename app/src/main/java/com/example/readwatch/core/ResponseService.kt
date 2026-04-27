package com.example.readwatch.core

sealed class ResponseService {
    data class Success(val value: Boolean)
    data class Error(val error: String)
}