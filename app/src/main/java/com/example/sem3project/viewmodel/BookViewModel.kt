package com.example.sem3project.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sem3project.model.BookModel
import com.example.sem3project.repo.BookRepo
import com.example.sem3project.utils.NotificationHelper

class BookViewModel(val repo: BookRepo) : ViewModel() {

    // Backup list for restoring unfiltered data
    private var originalList = listOf<BookModel>()

    // LiveData for a single book (detail screen)
    private val _books = MutableLiveData<BookModel?>()
    val books: LiveData<BookModel?> get() = _books

    // LiveData for HomeScreen & UserDashboard
    private val _dashboardBooks = MutableLiveData<List<BookModel>?>()
    val dashboardBooks: LiveData<List<BookModel>?> get() = _dashboardBooks

    // LiveData for admin / backup
    private val _allBooks = MutableLiveData<List<BookModel>?>()
    val allBooks: LiveData<List<BookModel>?> get() = _allBooks

    // 🔹 ADD BOOK (Admin – with notification)
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

    // 🔹 UPDATE BOOK
    fun updateBook(model: BookModel, callback: (Boolean, String) -> Unit) {
        repo.updateBook(model, callback)
    }

    // 🔹 DELETE BOOK
    fun deleteBook(bookID: String, callback: (Boolean, String) -> Unit) {
        repo.deleteBook(bookID, callback)
    }

    // 🔹 Get single book by ID
    fun getBookById(bookID: String) {
        repo.getBookById(bookID) { success, _, data ->
            if (success) {
                _books.postValue(data)
            }
        }
    }

    // 🔹 Get all books
    fun getAllProduct() {
        repo.getAllBooks { success, _, data ->
            if (success) {
                originalList = data
                _allBooks.postValue(data)
                _dashboardBooks.postValue(data)
            }
        }
    }

    // 🔹 Filter by genre (Search)
    fun filterByGenre(genreId: String) {
        if (genreId.isEmpty()) {
            _dashboardBooks.postValue(originalList)
        } else {
            _dashboardBooks.postValue(
                originalList.filter {
                    it.genreId.equals(genreId, ignoreCase = true)
                }
            )
        }
    }
}
