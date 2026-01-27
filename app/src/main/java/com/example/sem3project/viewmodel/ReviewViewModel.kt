package com.example.sem3project.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.sem3project.model.ReviewModel
import com.example.sem3project.repo.ReviewRepo
import com.example.sem3project.repo.ReviewRepoImpl

class ReviewViewModel(val repo: ReviewRepo= ReviewRepoImpl()): ViewModel() {
    private val _reviews = mutableStateOf<List<ReviewModel>>(emptyList())
    val reviews: State<List<ReviewModel>> = _reviews

//    initializer Block (Calls fetchReview fun immediately
    init {
        fetchReviews()
    }

    fun fetchReviews(){
        repo.fetchReviews { items ->
            _reviews.value =items
        }
    }

    fun saveOrUpdateReview(review: ReviewModel, isEdit: Boolean, callback: (Boolean) -> Unit) {
        repo.saveOrUpdateReview(review, isEdit) { success ->
            if (success) fetchReviews()
            callback(success)
        }
    }

    fun toggleBookInList(userId: String, bookId: String,
                         currentStatus: Boolean, callback: (Boolean) -> Unit){
        repo.toggleBookInList(userId, bookId,currentStatus, callback)

    }

    fun rateBook( bookId: String, rating: Double,
                  callback: (Boolean) -> Unit){
        repo.rateBook(bookId, rating, callback)
    }

    fun addReview(review: ReviewModel, onResult: (Boolean) -> Unit) {
        repo.addReview(review) { success ->
            if (success) fetchReviews()
            onResult(success)
        }
    }
}
