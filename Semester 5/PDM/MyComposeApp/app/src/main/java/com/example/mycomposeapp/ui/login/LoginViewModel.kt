package com.example.mycomposeapp.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycomposeapp.data.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: AuthRepository) : ViewModel() {

    var username by mutableStateOf("")
    var password by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    var loginSuccess by mutableStateOf(false)

    fun onLoginClick() {
        if (username.isBlank() || password.isBlank()) {
            errorMessage = "Please fill all the fields"
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            val result = repository.login(username, password)

            result.onSuccess {
                loginSuccess = true
            }.onFailure { e ->
                errorMessage = "Login failed: ${e.message}"
                isLoading = false
            }
        }
    }
}