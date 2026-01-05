package com.example.sem3project.repo

import com.example.sem3project.model.ReviewModel

interface ReviewRepo {
    fun fetchReviews(callback: (List<ReviewModel>)-> Unit)

    fun saveOrUpdateReview(
        review: ReviewModel,
        isEdit: Boolean,
        callback: (Boolean) -> Unit
    )

    fun toggleBookInList(
       userId: String,
       bookId: String,
       currentStatus: Boolean,
        callback: (Boolean) -> Unit
    )

    fun rateBook(
        bookId: String,
        rating: Double,
        callback: (Boolean) -> Unit
    )



}