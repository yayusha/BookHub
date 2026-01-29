package com.example.sem3project.model

data class ReviewModel(
    val reviewId: String = "",
    val userId: String ="",
    val id: String ="",
    val bookId: String = "",
    val date: String ="",
    val title: String="",
    val content: String="",
    val rating: Double= 5.0,
    val isReported: Boolean = false
){
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "reviewId" to reviewId,
            "userid" to userId,
            "id" to id,
            "bookId" to bookId,
            "date" to date,
            "title" to title,
            "content" to content,
            "rating" to rating,
            "isReported" to isReported
        )
    }
}
