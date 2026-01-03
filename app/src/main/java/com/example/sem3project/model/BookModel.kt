package com.example.sem3project.model

data class BookModel(
    var bookId: String = "",
    var bookName: String = "",
    var author: String = "",
    var description: String = "",
    var imageUrl: String = ""
){
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "bookName" to bookName,
            "author" to author,
            "description" to description,
            "imageUrl" to imageUrl
        )
    }
}


