package com.example.sem3project.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sem3project.model.BookModel
import com.example.sem3project.repo.BookRepo
import com.example.sem3project.utils.NotificationHelper

class BookViewModel(val repo: BookRepo) : ViewModel() {

    private var originalList = listOf<BookModel>()

    private val _books = MutableLiveData<BookModel?>()
    val books: LiveData<BookModel?> get() = _books

    private val _dashboardBooks = MutableLiveData<List<BookModel>?>()
    val dashboardBooks: LiveData<List<BookModel>?> get() = _dashboardBooks

    private val _allBooks = MutableLiveData<List<BookModel>?>()
    val allBooks: LiveData<List<BookModel>?> get() = _allBooks

    fun addBook(
        context: Context,
        model: BookModel,
        callback: (Boolean, String) -> Unit
    ) {
        repo.addBook(model) { success, message ->
            if (success) {
                NotificationHelper.showBookAddedNotification(context, model)
            }
            callback(success, message)
        }
    }

    fun updateBook(model: BookModel, callback: (Boolean, String) -> Unit) {
        repo.updateBook(model, callback)
    }

    fun deleteBook(bookID: String, callback: (Boolean, String) -> Unit) {
        repo.deleteBook(bookID, callback)
    }

    fun getBookById(bookID: String) {
        repo.getBookById(bookID) { success, _, data ->
            if (success) _books.postValue(data)
        }
    }

    fun getAllProduct() {
        repo.getAllBooks { success, _, data ->
            if (success) {
                originalList = data
                _allBooks.postValue(data)
                _dashboardBooks.postValue(data)
            }
        }
    }

    fun filterByGenre(genreId: String) {
        if (genreId.isEmpty()) _dashboardBooks.postValue(originalList)
        else _dashboardBooks.postValue(
            originalList.filter { it.genreId.equals(genreId, ignoreCase = true) }
        )
    }
}
