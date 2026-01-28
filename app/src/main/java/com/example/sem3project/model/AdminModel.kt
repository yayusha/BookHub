package com.example.sem3project.model

data class AdminModel(
    val userId: String = "",
    val firstName: String = "",
    val email: String = "",
    val role: String = "Admin",
    val status: String = "active"
)
