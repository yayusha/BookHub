//package com.example.sem3project.viewmodel
//
//import android.content.Context
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import com.example.sem3project.model.BookModel
//import com.example.sem3project.repo.BookRepo
//import com.example.sem3project.utils.NotificationHelper
//
//class BookViewModel(val repo: BookRepo) : ViewModel(){
//
//    fun addBook(context: Context, model: BookModel, callback: (Boolean, String) -> Unit){
//        repo.addBook(model) { success, message ->
//            if (success) {
//                // Show notification after book is successfully added
//                NotificationHelper.showBookAddedNotification(context, model)
//            }
//            callback(success, message)
//        }
//    }
//
//    fun updateBook(model: BookModel, callback: (Boolean, String) -> Unit){
//        repo.updateBook(model,callback)
//    }
//
//    fun deleteBook(bookID: String, callback: (Boolean, String) -> Unit){
//        repo.deleteBook(bookID,callback)
//    }
//
//    private val _books = MutableLiveData<BookModel?>()
//    val books : MutableLiveData<BookModel?> get() = _books
//
//    private val _allBooks = MutableLiveData<List<BookModel>?>()
//    val allBooks: MutableLiveData<List<BookModel>?> get() = _allBooks
//
//    fun getBookById(bookID: String){
//        repo.getBookById(bookID){
//                success,message,data->
//            if(success){
//                _books.postValue(data)
//            }
//        }
//    }
//
//    fun getAllProduct(){
//        repo.getAllBooks{
//                success,message,data->
//            if(success){
//                _allBooks.postValue(data)
//            }
//        }
//    }
//}