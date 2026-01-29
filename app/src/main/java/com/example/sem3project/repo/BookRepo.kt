package com.example.sem3project.repo

import com.example.sem3project.model.BookModel
import com.example.sem3project.model.WishlistBook

interface BookRepo {
    fun addBook(model: BookModel, callback: (Boolean, String) -> Unit)
    fun getAllBooks(callback: (Boolean,String, List<BookModel>) -> Unit)
    fun getBookById(bookID: String, callback: (Boolean, String,BookModel?) -> Unit)
    fun updateBook(model: BookModel, callback: (Boolean, String) -> Unit)
    fun deleteBook(bookID: String, callback: (Boolean, String) -> Unit)

    fun getBookByGenre(genreId:String,callback:(Boolean,String,List<BookModel>?) -> Unit)

    fun fetchBooks(callback: (List<BookModel>) -> Unit)

    fun toggleWishlist(bookId: String,
                       userId: String,
                       book: WishlistBook,
                       callback: (Boolean) -> Unit)

    fun toggleReadStatus(bookId: String, userId: String, callback: (Boolean) -> Unit)
}