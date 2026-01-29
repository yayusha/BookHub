package com.example.sem3project.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.sem3project.model.ReplyModel
import com.example.sem3project.model.ReviewModel
import com.example.sem3project.repo.ReviewRepo
import com.example.sem3project.repo.ReviewRepoImpl
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class ReviewViewModel(val repo: ReviewRepo = ReviewRepoImpl()) : ViewModel() {

    private val _reviews = mutableStateOf<List<ReviewModel>>(emptyList())
    val reviews: State<List<ReviewModel>> = _reviews

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _deleteStatus = mutableStateOf<String?>(null)
    val deleteStatus: State<String?> = _deleteStatus

    // --- Notifications ---
    private val _notifications = mutableStateOf<List<Map<String, Any>>>(emptyList())
    val notifications: State<List<Map<String, Any>>> = _notifications

    // --- Review functions ---
    fun fetchReviews() {
        repo.fetchReviews { items ->
            _reviews.value = items
            _isLoading.value = false
        }
    }

    fun addReview(review: ReviewModel, bookId: String, onResult: (Boolean) -> Unit) {
        repo.addReview(review) { success ->
            if (success) fetchBookSpecificReviews(bookId)
            onResult(success)
        }
    }

    fun saveOrUpdateReview(
        review: ReviewModel,
        isEdit: Boolean,
        bookId: String,
        callback: (Boolean) -> Unit
    ) {
        repo.saveOrUpdateReview(review, isEdit) { success ->
            if (success) fetchBookSpecificReviews(bookId)
            callback(success)
        }
    }

    fun deleteReview(reviewId: String, bookId: String) {
        repo.deleteReview(reviewId) { success ->
            if (success) fetchBookSpecificReviews(bookId)
        }
    }

    fun fetchBookSpecificReviews(bookId: String) {
        _isLoading.value = true
        repo.fetchReviewsByBook(bookId) { items ->
            _reviews.value = items
            _isLoading.value = false
        }
    }

    fun toggleLike(review: ReviewModel, currentUserId: String) {
        if (review.userId == currentUserId) return

        val updatedLikes = review.likedBy.toMutableMap()
        val isCurrentlyLiked = updatedLikes.containsKey(currentUserId)

        if (isCurrentlyLiked) {
            updatedLikes.remove(currentUserId)
        } else {
            updatedLikes[currentUserId] = true
        }

        // We must keep the SAME reviewId so Firebase updates the existing record
        val updatedReview = review.copy(likedBy = updatedLikes)

        // Ensure 'isEdit' is true so the Repo knows to update, not add new
        repo.saveOrUpdateReview(updatedReview, isEdit = true) { success ->
            if (success) {
                fetchBookSpecificReviews(review.bookId)
                // Only notify if we just added a like, not when unliking
                if (!isCurrentlyLiked) {
                    sendLikeNotification(review.userId, currentUserId, review.reviewId)
                }
            }
        }
    }

    private fun sendLikeNotification(toUserId: String, fromUserId: String, reviewId: String) {
        if (toUserId == fromUserId) return

        val notifRef = FirebaseDatabase.getInstance()
            .getReference("notifications")
            .child(toUserId)
            .push()

        val notif = mapOf(
            "type" to "like",
            "fromUserId" to fromUserId,
            "reviewId" to reviewId,
            "date" to SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date()),
            "read" to false
        )
        notifRef.setValue(notif)
    }

    fun addReply(review: ReviewModel, replyText: String, currentUserId: String, onComplete: (Boolean) -> Unit) {
        if (review.userId == currentUserId) return

        val replyRef = FirebaseDatabase.getInstance()
            .getReference("reviewReplies")
            .child(review.reviewId)
            .push()

        val reply = ReplyModel(
            replyId = replyRef.key ?: "",
            reviewId = review.reviewId,
            userId = currentUserId,
            content = replyText,
            date = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date())
        )

        replyRef.setValue(reply).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                sendReplyNotification(review.userId, currentUserId, review.reviewId, reply.replyId)
            }
            onComplete(task.isSuccessful)
        }
    }

    private fun sendReplyNotification(toUserId: String, fromUserId: String, reviewId: String, replyId: String) {
        if (toUserId == fromUserId) return

        val notifRef = FirebaseDatabase.getInstance()
            .getReference("notifications")
            .child(toUserId)
            .push()

        val notif = mapOf(
            "type" to "reply",
            "fromUserId" to fromUserId,
            "reviewId" to reviewId,
            "replyId" to replyId,
            "date" to SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date()),
            "read" to false
        )
        notifRef.setValue(notif)
    }

    fun fetchReplies(reviewId: String, onResult: (List<ReplyModel>) -> Unit) {
        val dbRef = FirebaseDatabase.getInstance().getReference("reviewReplies").child(reviewId)
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val replies = snapshot.children.mapNotNull { it.getValue(ReplyModel::class.java) }
                onResult(replies)
            }

            override fun onCancelled(error: DatabaseError) {
                onResult(emptyList())
            }
        })
    }

    fun fetchNotifications(currentUserId: String) {
        val notifRef = FirebaseDatabase.getInstance().getReference("notifications").child(currentUserId)
        notifRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children.mapNotNull { it.value as? Map<String, Any> }
                    .sortedByDescending { it["date"] as? String }
                _notifications.value = list
            }

            override fun onCancelled(error: DatabaseError) {
                _notifications.value = emptyList()
            }
        })
    }
    fun reportReview(reviewId: String) {
        repo.updateReviewStatus(reviewId, true) { success ->
            if (success) {
                _reviews.value = _reviews.value.map {
                    if (it.reviewId == reviewId) it.copy(isReported  = true) else it
                }
            }
        }
    }

    fun clearDeleteStatus() {
        _deleteStatus.value = null
    }

    // Also, make sure you have the resolveReview function if it's missing:
    fun resolveReview(reviewId: String) {
        repo.updateReviewStatus(reviewId, false) { success ->
            if (success) {
                // Update the local state so the UI reflects the change immediately
                _reviews.value = _reviews.value.map {
                    if (it.reviewId == reviewId) it.copy(isReported = false) else it
                }
            }
        }
    }
}
