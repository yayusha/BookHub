package com.example.sem3project.model

data class ProfileModel(
    var userId: String = "",
    var username: String = "",
    var email: String = "",
    var bio: String = "",
    var profileImage: String = ""
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "userId" to userId,
            "username" to username,
            "email" to email,
            "bio" to bio,
            "profileImage" to profileImage
        )
    }
}
