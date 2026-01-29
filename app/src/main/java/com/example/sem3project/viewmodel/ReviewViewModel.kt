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
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _deleteStatus = mutableStateOf<String?>(null)
    val deleteStatus: State<String?> = _deleteStatus

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

    fun deleteReview(reviewId: String) {
        repo.deleteReview(reviewId) { success ->
            if (success) {
                _deleteStatus.value = "Review deleted successfully"
                fetchReviews()
            } else {
                _deleteStatus.value = "Failed to delete review"
            }
        }
    }

    fun clearDeleteStatus() {
        _deleteStatus.value = null
    }

    fun resolveReview(reviewId: String) {
        repo.updateReviewStatus(reviewId, false) { success ->
            if (success) {
                _reviews.value = _reviews.value.map {
                    if (it.id == reviewId) it.copy(isReported = false) else it
                }
            }
        }
    }

    fun fetchBookSpecificReviews(bookId: String) {
        _isLoading.value = true
        repo.fetchReviewsByBook(bookId) { items ->
            _reviews.value = items
            _isLoading.value = false
        }
    }

}
