package com.example.sem3project.model

data class ReplyModel(
    val replyId: String = "",
    val reviewId: String = "",
    val userId: String = "",
    val content: String = "",
    val date: String = ""
){
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "replyId" to replyId,
            "reviewId" to reviewId,
            "userId" to userId,
            "date" to date,
            "content" to content
        )
    }
}
