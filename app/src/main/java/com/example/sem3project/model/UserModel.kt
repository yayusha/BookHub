package com.example.sem3project.model

data class UserModel(
    val userId: String? = "",
    val email: String? = "",
    val firstName: String? = "",
    val lastName: String? = "",
    val phoneNumber: String? = "",
    val status: String = "active",
    val role: String = "user"
) {
    val isBanned: Boolean
        get() = status == "banned"

    val isAdmin: Boolean
        get() = role == "admin"

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "userId" to userId,
            "email" to email,
            "firstName" to firstName,
            "lastName" to lastName,
            "phoneNumber" to phoneNumber,
            "status" to status,
            "role" to role
        )
    }
}