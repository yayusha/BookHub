package com.example.sem3project.model

data class ReviewModel(
    val reviewId: String = "",
    val userId: String ="",
    val bookId: String = "",
    val date: String ="",
    val title: String="",
    val content: String="",
    val rating: Double= 5.0,
    val isReported: Boolean = false,

    val likedBy: Map<String, Boolean> = emptyMap()
){
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "reviewId" to reviewId,
            "userId" to userId,
            "bookId" to bookId,
            "date" to date,
            "title" to title,
            "content" to content,
            "rating" to rating,
            "isReported" to isReported,
            "likedBy" to likedBy
        )
    }
}
