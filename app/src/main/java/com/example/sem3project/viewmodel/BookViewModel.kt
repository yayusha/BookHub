package com.example.sem3project.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sem3project.model.BookModel
import com.example.sem3project.repo.BookRepo

class BookViewModel(val repo: BookRepo) : ViewModel() {

    // Backup list for restoring unfiltered data
    private var originalList = listOf<BookModel>()

    // LiveData for a single book (detail screen)
    private val _books = MutableLiveData<BookModel?>()
    val books: LiveData<BookModel?> get() = _books

    // LiveData for HomeScreen & UserDashboard (filtered/unfiltered)
    private val _dashboardBooks = MutableLiveData<List<BookModel>?>()
    val dashboardBooks: LiveData<List<BookModel>?> get() = _dashboardBooks

    // LiveData for all books (admin or backup)
    private val _allBooks = MutableLiveData<List<BookModel>?>()
    val allBooks: LiveData<List<BookModel>?> get() = _allBooks

    // 🔹 CRUD Operations
    fun addBook(model: BookModel, callback: (Boolean, String) -> Unit) {
        repo.addBook(model, callback)
    }

    fun updateBook(model: BookModel, callback: (Boolean, String) -> Unit) {
        repo.updateBook(model, callback)
    }

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

    // 🔹 Get all books (fetch once)
    fun getAllProduct() {
        repo.getAllBooks { success, _, data ->
            if (success) {
                originalList = data            // store backup for filtering
                _allBooks.postValue(data)
                _dashboardBooks.postValue(data) // HomeScreen shows this
            }
        }
    }

    // 🔹 Filter by genre (used for search)
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
