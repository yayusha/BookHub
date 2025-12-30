package com.example.sem3project.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.sem3project.model.Review
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BookViewModel : ViewModel() {
    private val database = FirebaseDatabase.getInstance().getReference("reviews")

    private val _reviews = mutableStateOf<List<Review>>(emptyList())
    val reviews: State<List<Review>> = _reviews

    fun fetchReviews() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = snapshot.children.mapNotNull { data ->
                    data.getValue(Review::class.java)?.copy(id = data.key ?: "")
                }
                _reviews.value = items
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun saveOrUpdateReview(review: Review, isEdit: Boolean, callback: (Boolean) -> Unit) {
        val ref = if (isEdit && review.id.isNotEmpty()) {
            database.child(review.id)
        } else {
            database.push()
        }

        val finalReview = if (isEdit) review else review.copy(id = ref.key ?: "")

        ref.setValue(finalReview).addOnCompleteListener { task ->
            callback(task.isSuccessful)
        }
    }
}