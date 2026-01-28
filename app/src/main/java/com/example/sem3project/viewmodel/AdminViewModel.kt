package com.example.sem3project.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.sem3project.model.UserModel
import com.example.sem3project.repo.AdminRepo
import com.example.sem3project.repo.AdminRepoImpl

class AdminViewModel(private val repo: AdminRepo = AdminRepoImpl()) : ViewModel() {

    val usersList = mutableStateOf<List<UserModel>>(emptyList())
    val actionStatus = mutableStateOf<Pair<Boolean, String>?>(null)

    fun fetchUsers() {
        repo.fetchAllUsers { users ->
            usersList.value = users
        }
    }

    fun banUser(userId: String) {
        repo.banUser(userId) { success, msg ->
            actionStatus.value = Pair(success, msg)
            if (success) fetchUsers()
        }
    }

    fun unbanUser(userId: String) {
        repo.unbanUser(userId) { success, msg ->
            actionStatus.value = Pair(success, msg)
            if (success) fetchUsers()
        }
    }
}
