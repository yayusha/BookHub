package com.example.sem3project.model

data class ProfileModel(
    var userId: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var email: String = "",
    var bio: String = "",
    var imageUrl: String = ""
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "userId" to userId,
            "firstName" to firstName,
            "lastName" to lastName,
            "email" to email,
            "bio" to bio,
            "imageUrl" to imageUrl,
        )
    }
}
