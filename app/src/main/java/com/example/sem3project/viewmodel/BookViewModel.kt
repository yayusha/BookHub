package com.example.sem3project.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sem3project.model.BookModel
import com.example.sem3project.repo.BookRepo
import com.example.sem3project.utils.NotificationHelper
import com.google.firebase.database.FirebaseDatabase

class BookViewModel(
    private val repo: BookRepo) : ViewModel() {

    // ---------- SINGLE BOOK ----------
    private val _selectedBook = MutableLiveData<BookModel?>()
    val selectedBook: LiveData<BookModel?> get() = _selectedBook

    // ---------- ADMIN DASHBOARD ----------
    private val _dashboardBooks = MutableLiveData<List<BookModel>>()
    val dashboardBooks: LiveData<List<BookModel>> get() = _dashboardBooks

    private val _allBooks = MutableLiveData<List<BookModel>>()
    val allBooks: LiveData<List<BookModel>> get() = _allBooks

    private var originalList = listOf<BookModel>()

    // ---------- USER HOME (COMPOSE) ----------
    var bookListState = mutableStateOf<List<BookModel>>(emptyList())
        private set

    var selectedBookState = mutableStateOf<BookModel?>(null)
        private set

    init {
        fetchBooks()
    }

    private fun fetchBooks() {
        repo.fetchBooks { list ->
            bookListState.value = list
        }
    }

    // ---------- CRUD ----------
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
        repo.getBookById(bookID) { success, _, book ->
            if (success && book != null) {
                selectedBookState.value = book
                _selectedBook.postValue(book)
            }
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

    fun toggleReadStatus(bookId: String, userId: String) {
        val ref = FirebaseDatabase.getInstance()
            .getReference("user_activities")
            .child(userId)
            .child("read_books")
            .child(bookId)

        ref.setValue(true)
    }

    fun toggleWishlist(bookId: String, userId: String) {
        val ref = FirebaseDatabase.getInstance()
            .getReference("user_activities")
            .child(userId)
            .child("wishlist")
            .child(bookId)

        ref.setValue(true)
    }
}
