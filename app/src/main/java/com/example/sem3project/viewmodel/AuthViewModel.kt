package com.example.sem3project.viewmodel

import androidx.lifecycle.ViewModel
import com.example.sem3project.repo.authrepo
import com.example.sem3project.repo.authrepoimpl

class AuthViewModel(val repo: authrepo = authrepoimpl()) : ViewModel() {

    fun login(email: String, pass: String, callback: (Boolean, String) -> Unit) {
        repo.login(email, pass, callback)
    }

    fun register(email: String, pass: String, callback: (Boolean, String, String) -> Unit) {
        repo.register(email, pass, callback)
    }

    fun forgotPassword(email: String, callback: (Boolean, String) -> Unit) {
        repo.forgotPassword(email, callback)
    }

}