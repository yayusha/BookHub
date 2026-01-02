package com.example.sem3project.repo

import com.example.sem3project.model.BookModel

interface ProductRepo {
    fun addBook(model: BookModel, onResult: (Boolean, String) -> Unit)
    fun getAllBooks(onResult: (Boolean,String, List<BookModel>) -> Unit)
    fun getBookById(bookId: String, onResult: (Boolean, String,BookModel?) -> Unit)
    fun updateBook(model: BookModel, onResult: (Boolean, String) -> Unit)
    fun deleteBook(bookId: String, onResult: (Boolean, String) -> Unit)
}