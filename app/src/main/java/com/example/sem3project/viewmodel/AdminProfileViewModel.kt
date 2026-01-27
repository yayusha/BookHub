package com.example.sem3project.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sem3project.model.AdminModel
import com.example.sem3project.repo.AdminProfileRepo
import com.example.sem3project.repo.AdminProfileRepoImpl

class AdminProfileViewModel(
    private val repo: AdminProfileRepo = AdminProfileRepoImpl()
) : ViewModel() {

    val adminData = mutableStateOf(AdminModel())

    val status = mutableStateOf<Pair<Boolean, String>?>(null)

    val isLoading = mutableStateOf(false)

    fun fetchAdminProfile() {
        isLoading.value = true

        repo.fetchAdminProfile { success, admin, msg ->
            isLoading.value = false

            if (success && admin != null) {
                adminData.value = admin
                status.value = Pair(true, "Profile loaded")
            } else {
                status.value = Pair(false, msg)
            }
        }
    }

    fun updateAdminProfile(name: String, username: String, email: String, role: String) {
        isLoading.value = true
        adminData.value = adminData.value.copy(
            name = name,
            username = username,
            email = email,
            role = role,

        )
        isLoading.value = false
    }
    fun logout() {

        adminData.value = AdminModel()
        _isLoggedOut.value = true
    }

    private val _isLoggedOut = MutableLiveData(false)
    val isLoggedOut: MutableLiveData<Boolean> = _isLoggedOut
}



