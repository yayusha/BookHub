package com.example.sem3project.repo

import com.example.sem3project.model.AdminModel

interface AdminProfileRepo {

    fun fetchAdminProfile(
        callback: (Boolean, AdminModel?, String) -> Unit
    )

    fun updateAdminProfile(
        name: String,
        callback: (Boolean, String) -> Unit
    )
}
