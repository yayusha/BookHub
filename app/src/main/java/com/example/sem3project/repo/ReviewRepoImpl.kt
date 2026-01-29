package com.example.sem3project.repo

import com.example.sem3project.model.ReviewModel
import com.google.firebase.database.*

class ReviewRepoImpl : ReviewRepo {

    private val database = FirebaseDatabase.getInstance().getReference("reviews")
    private val userListDb = FirebaseDatabase.getInstance().getReference("user_lists")
    private val booksDb = FirebaseDatabase.getInstance().getReference("books")

    override fun fetchReviews(callback: (List<ReviewModel>) -> Unit) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = snapshot.children.mapNotNull { data ->
                    data.getValue(ReviewModel::class.java)?.copy(reviewId = data.key ?: "")
                }
                callback(items)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(emptyList())
            }
        })
    }

    override fun addReview(review: ReviewModel, callback: (Boolean) -> Unit) {
        val reviewId = database.push().key ?: ""
        val finalReview = review.copy(reviewId = reviewId)

        database.child(reviewId).setValue(finalReview.toMap())
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    override fun saveOrUpdateReview(
        review: ReviewModel,
        isEdit: Boolean,
        callback: (Boolean) -> Unit
    ) {
        val ref = if (isEdit && review.reviewId.isNotEmpty()) {
            database.child(review.reviewId)
        } else {
            database.push()
        }
        val finalReview = if (isEdit) {
            review
        } else {
            review.copy(reviewId = ref.key ?: "")
        }
        ref.setValue(finalReview.toMap())
            .addOnCompleteListener { callback(it.isSuccessful) }
    }


    override fun toggleBookInList(userId: String, bookId: String, currentStatus: Boolean, callback: (Boolean) -> Unit) {
        val ref = userListDb.child(userId).child(bookId)
        if (currentStatus) {
            ref.removeValue().addOnCompleteListener { callback(it.isSuccessful) }
        } else {
            ref.setValue(true).addOnCompleteListener { callback(it.isSuccessful) }
        }
    }

    override fun rateBook(bookId: String, rating: Double, callback: (Boolean) -> Unit) {
        booksDb.child(bookId).child("rating").setValue(rating)
            .addOnCompleteListener { callback(it.isSuccessful) }
    }

    override fun deleteReview(reviewId: String, callback: (Boolean) -> Unit) {
        database.child(reviewId).removeValue()
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener {
                callback(false)
            }
    }

    override fun updateReviewStatus(reviewId: String, isReported: Boolean, callback: (Boolean) -> Unit) {
        val dbRef = FirebaseDatabase.getInstance().getReference("reviews").child(reviewId)

        val update = mapOf("isReported" to isReported)

        dbRef.updateChildren(update).addOnCompleteListener { task ->
            callback(task.isSuccessful)
        }
    }

    override fun fetchReviewsByBook(
        bookId: String,
        callback: (List<ReviewModel>) -> Unit
    ) {
        database.orderByChild("bookId").equalTo(bookId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val items = snapshot.children.mapNotNull { data ->
                        data.getValue(ReviewModel::class.java)?.copy(reviewId = data.key ?: "")
                    }
                    callback(items)
                }
                override fun onCancelled(error: DatabaseError) {
                    callback(emptyList())
                }
            })
    }
}
