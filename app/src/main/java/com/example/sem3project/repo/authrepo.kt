package com.example.sem3project.repo


interface authrepo {
    fun login
                (email: String,
                 password: String,
                 callback: (Boolean, String) -> Unit)

    fun register(email: String,
                 password: String,
                 callback: (Boolean, String, String) -> Unit)

    fun forgotPassword(email: String,
                       callback: (Boolean, String) -> Unit)
}