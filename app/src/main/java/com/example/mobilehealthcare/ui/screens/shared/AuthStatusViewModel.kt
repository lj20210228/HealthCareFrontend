package com.example.mobilehealthcare.ui.screens.shared

import androidx.lifecycle.ViewModel
import com.example.mobilehealthcare.service.TokenStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthStatusViewModel @Inject
constructor(val tokenStorage: TokenStorage,
    ) {
        private val _isLoggedIn= MutableStateFlow(tokenStorage.getToken()!=null)
    val isLoggedIn=_isLoggedIn.asStateFlow()
    private val _userRole= MutableStateFlow(tokenStorage.getRole())
    val userRole=_userRole.asStateFlow()
    fun updateAuthStatus(){
        _isLoggedIn.value=tokenStorage.getToken()!=null
        _userRole.value=tokenStorage.getRole()

    }
}