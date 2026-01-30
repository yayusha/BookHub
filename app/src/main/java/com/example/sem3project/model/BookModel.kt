package com.example.sem3project.model

data class BookModel(
    var bookId: String = "",
    var bookName: String = "",
    var author: String = "",
    var genreId: String = "",
    val summary: String? = "",
    var description: String = "",
    var imageUrl: String = "",
    val authorBio: String = ""
){
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "bookName" to bookName,
            "author" to author,
            "genreId" to genreId,
            "summary" to summary,
            "description" to description,
            "imageUrl" to imageUrl,
            "authorBio" to authorBio
        )
    }
}


