package com.example.sem3project.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sem3project.model.UserModel
import com.example.sem3project.repo.AuthRepo
import com.example.sem3project.repo.AuthRepoImpl

class AuthViewModel(val repo: AuthRepo = AuthRepoImpl()) : ViewModel() {

    // 🔹 Authentication methods
    fun login(email: String, pass: String, callback: (Boolean, String) -> Unit) {
        repo.login(email = email, pass, callback = callback)
    }

    fun register(email: String, pass: String, callback: (Boolean, String, String) -> Unit) {
        repo.register(email, pass, callback)
    }

    fun forgetPassword(email: String, callback: (Boolean, String) -> Unit) {
        repo.forgotPassword(email, callback)
    }

    fun deleteAccount(userId: String, callback: (Boolean, String) -> Unit) {
        repo.deleteAccount(userId, callback)
    }

    fun addUserToDatabase(userId: String, model: UserModel, callback: (Boolean, String) -> Unit) {
        repo.addUserToDatabase(userId, model, callback)
    }

    fun editProfile(userId: String, model: UserModel, callback: (Boolean, String) -> Unit) {
        repo.editProfile(userId, model, callback)
    }

    fun deactivateUser(userId: String, callback: (Boolean, String) -> Unit) {
        repo.deactivateUser(userId, callback)
    }

    // 🔹 LiveData for single user
    private val _users = MutableLiveData<UserModel?>()
    val users: MutableLiveData<UserModel?> get() = _users

    // 🔹 LiveData for all users (Admin)
    private val _allUsers = MutableLiveData<List<UserModel>?>()
    val allUsers: MutableLiveData<List<UserModel>?> get() = _allUsers

    // 🔹 Get user by ID
    fun getUserById(userId: String) {
        repo.getUserById(userId) { success, _, data ->
            if (success) {
                _users.postValue(data)
            }
        }
    }
}
