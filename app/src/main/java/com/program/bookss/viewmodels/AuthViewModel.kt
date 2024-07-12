package com.program.bookss.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.program.bookss.repository.AuthRepository
import com.program.bookss.repository.AuthRes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val result = authRepository.login(email, password)
            when (result) {
                is AuthRes.Success -> {
                    // Manejar el inicio de sesión exitoso
                }
                is AuthRes.Error -> {
                    // Manejar el error
                }
            }
        }
    }

    fun register(email: String, password: String,context: Context) {
        viewModelScope.launch {
            val result = authRepository.register(email, password)
            when (result) {
                is AuthRes.Success -> {
                    Toast.makeText(context, "Registro Exitoso", Toast.LENGTH_SHORT).show()
                }
                is AuthRes.Error -> {
                    Toast.makeText(context, "Error SignUp: ${result.errorMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun logout() {
        authRepository.logout()
    }
}