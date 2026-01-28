package com.example.sem3project.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.sem3project.model.AdminModel
import com.example.sem3project.model.UserModel
import com.example.sem3project.repo.AdminRepo
import com.example.sem3project.repo.AdminRepoImpl

class AdminViewModel(private val repo: AdminRepo = AdminRepoImpl()) : ViewModel() {

    val usersList = mutableStateOf<List<UserModel>>(emptyList())
    val actionStatus = mutableStateOf<Pair<Boolean, String>?>(null)

    val adminData = mutableStateOf(AdminModel())
    val isLoading = mutableStateOf(false)
    val profileStatus = mutableStateOf<Pair<Boolean, String>?>(null)

    var searchQuery = mutableStateOf("")

    val filteredUsers: List<UserModel>
        get() {
            val query = searchQuery.value.lowercase().trim()

            return if (query.isEmpty()) {
                usersList.value
            } else {
                usersList.value.filter { user ->
                    val firstName = user.firstName?.lowercase() ?: ""
                    val email = user.email?.lowercase() ?: ""

                    firstName.contains(query) || email.contains(query)
                }
            }
        }

    init {
        fetchUsers()
        fetchAdminProfile()
    }

    fun fetchUsers() {
        repo.fetchAllUsers { users -> usersList.value = users }
    }

    fun fetchAdminProfile() {
        isLoading.value = true
        repo.fetchAdminProfile { success, admin, msg ->
            isLoading.value = false
            if (success && admin != null) {
                adminData.value = admin
            } else {
                profileStatus.value = Pair(false, msg)
            }
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
            if (success) {
                fetchUsers()
            }
        }
    }

    fun updateAdminProfile(newName: String) {
        isLoading.value = true
        repo.updateAdminProfile(newName) { success, msg ->
            isLoading.value = false
            actionStatus.value = Pair(success, msg)
            if (success) {
                adminData.value = adminData.value.copy(firstName = newName)
            }
        }
    }

}