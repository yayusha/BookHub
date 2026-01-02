package com.example.sem3project.model

data class BookModel(
    val bookId: String = "",
    val bookName: String = "",
    val author: String = "",
    val description: String = "",
    val imageUrl: String? = null
){
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "bookId" to bookId,
            "bookName" to bookName,
            "author" to author,
            "description" to description,
            "imageUrl" to imageUrl
        )
    }
}


