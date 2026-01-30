package com.example.sem3project.repo

import com.example.sem3project.model.UserModel


interface AuthRepo {
    fun login
                (email: String,
                 password: String,
                 callback: (Boolean, String) -> Unit)

    fun register(email: String,
                 password: String,
                 callback: (Boolean, String, String) -> Unit)

    fun addUserToDatabase(
        userId: String, model: UserModel,
        callback: (Boolean, String) -> Unit
    )

    fun deleteAccount(userId: String, callback: (Boolean, String) -> Unit)

    fun editProfile(
        userId: String, model: UserModel,
        callback: (Boolean, String) -> Unit
    )

    fun getUserById(
        userId: String,
        callback: (Boolean, String, UserModel?) -> Unit
    )
    fun forgotPassword(email: String,
                       callback: (Boolean, String) -> Unit)
    fun deactivateUser (
        userId: String,
        callback: (Boolean, String) -> Unit)

}