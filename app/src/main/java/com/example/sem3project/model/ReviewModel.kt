package com.example.sem3project.model

data class ReviewModel(
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
