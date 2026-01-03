package com.example.sem3project.repo

import com.example.sem3project.model.BookModel

interface BookRepo {
    fun addBook(model: BookModel, callback: (Boolean, String) -> Unit)
    fun getAllBooks(callback: (Boolean,String, List<BookModel>) -> Unit)
    fun getBookById(bookID: String, callback: (Boolean, String,BookModel?) -> Unit)
    fun updateBook(model: BookModel, callback: (Boolean, String) -> Unit)
    fun deleteBook(bookID: String, callback: (Boolean, String) -> Unit)
}