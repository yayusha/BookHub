package com.example.sem3project.model

data class BookModel(
    var bookId: String = "",
    var bookName: String = "",
    var author: String = "",
    var genreId: String = "",
    var description: String = "",
    var imageUrl: String = ""
){
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "bookName" to bookName,
            "author" to author,
            "genreId" to genreId,
            "description" to description,
            "imageUrl" to imageUrl
        )
    }
}


