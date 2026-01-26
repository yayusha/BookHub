package com.example.sem3project.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sem3project.model.BookModel
import com.example.sem3project.repo.BookRepo



class BookViewModel(val repo: BookRepo) : ViewModel(){
    fun addBook(model: BookModel, callback: (Boolean, String) -> Unit){
        repo.addBook(model,callback)
    }

    fun updateBook(model: BookModel, callback: (Boolean, String) -> Unit){
        repo.updateBook(model,callback)
    }

    fun deleteBook(bookID: String, callback: (Boolean, String) -> Unit){
        repo.deleteBook(bookID,callback)
    }

    private val _books = MutableLiveData<BookModel?>()
    val books : MutableLiveData<BookModel?> get() = _books

    private val _allBooks = MutableLiveData<List<BookModel>?>()
    val allBooks: MutableLiveData<List<BookModel>?> get() = _allBooks

    fun getBookById(bookID: String){
        repo.getBookById(bookID){
                success,message,data->
            if(success){
                _books.postValue(data)
            }
        }
    }

    fun getAllProduct(){
        repo.getAllBooks{
                success,message,data->
            if(success){
                _allBooks.postValue(data)
            }
        }
    }}

