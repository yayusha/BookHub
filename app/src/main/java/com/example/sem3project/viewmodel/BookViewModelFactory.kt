package com.example.sem3project.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sem3project.repo.BookRepo

class BookViewModelFactory(private val repo: BookRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BookViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
