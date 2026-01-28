package com.example.sem3project.repo

import com.example.sem3project.model.UserModel

interface AdminRepo {
    fun fetchAllUsers(callback: (List<UserModel>) -> Unit)

    fun banUser(userId: String, callback: (Boolean, String) -> Unit)

    fun unbanUser(userId: String, callback: (Boolean, String) -> Unit)
}
