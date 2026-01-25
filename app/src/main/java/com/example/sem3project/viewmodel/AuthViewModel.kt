package com.example.sem3project.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sem3project.model.UserModel
import com.example.sem3project.repo.authrepo
import com.example.sem3project.repo.authrepoimpl

class AuthViewModel(val repo: authrepo = authrepoimpl()) : ViewModel() {

    fun login(email: String, pass: String, callback: (Boolean, String) -> Unit) {
        repo.login(email=email, pass, callback=callback)
    }
    fun register(email: String, pass: String, callback: (Boolean, String, String) -> Unit) {
        repo.register(email, pass, callback)
    }

    fun addUserToDatabase(
        userId: String, model: UserModel,
        callback: (Boolean, String) -> Unit
    ) {
        repo.addUserToDatabase(userId, model, callback)
    }

    fun forgetPassword(
        email: String,
        callback: (Boolean, String) -> Unit
    ) {
        repo.forgotPassword(email, callback)
    }

    fun deleteAccount(userId: String, callback: (Boolean, String) -> Unit) {
        repo.deleteAccount(userId, callback)
    }

    fun editProfile(
        userId: String, model: UserModel,
        callback: (Boolean, String) -> Unit
    ) {
        repo.editProfile(userId, model, callback)
    }

    private val _users = MutableLiveData<UserModel?>()
    val users: MutableLiveData<UserModel?>
        get() = _users

    private val _allUsers = MutableLiveData<List<UserModel>?>()
    val allUsers: MutableLiveData<List<UserModel>?>
        get() = _allUsers

    fun getUserById(
        userId: String
    ) {
        repo.getUserById(userId) { success, msg, data ->
            if (success) {
                _users.postValue(data)
            }
        }

        }
    fun deactivateUser(userId: String, callback: (Boolean, String) -> Unit) {
        repo.deactivateUser(userId) { success, message ->
            callback(success, message)
        }
    }
    }
