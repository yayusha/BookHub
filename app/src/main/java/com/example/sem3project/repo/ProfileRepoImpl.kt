package com.example.sem3project.repo

import com.example.sem3project.model.ProfileModel
import com.example.sem3project.model.ReviewModel
import com.example.sem3project.model.WishlistBook
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileRepoImpl : ProfileRepo {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val ref: DatabaseReference = database.getReference("Users")

    override fun addProfile(
        model: ProfileModel,
        callback: (Boolean, String) -> Unit
    ) {
        val id = model.userId.ifEmpty {
            ref.push().key ?: run {
                callback(false, "Failed to generate user ID")
                return
            }
        }

        model.userId = id

        ref.child(id)
            .setValue(model)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    callback(true, "Profile added successfully")
                } else {
                    callback(false, it.exception?.message ?: "Something went wrong")
                }
            }
    }

    override fun getProfileById(
        userId: String,
        callback: (Boolean, String, ProfileModel?) -> Unit
    ) {
        ref.child(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val profile = snapshot.getValue(ProfileModel::class.java)
                        callback(true, "Profile fetched successfully", profile)
                    } else {
                        callback(false, "Profile not found", null)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(false, error.message, null)
                }
            })
    }

    override fun updateProfile(
        model: ProfileModel,
        callback: (Boolean, String) -> Unit
    ) {
        if (model.userId.isEmpty()) {
            callback(false, "User ID is missing")
            return
        }

        ref.child(model.userId)
            .updateChildren(model.toMap())
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    callback(true, "Profile updated successfully")
                } else {
                    callback(false, it.exception?.message ?: "Update failed")
                }
            }
    }

    override fun deleteProfile(
        userId: String,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(userId)
            .removeValue()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    callback(true, "Profile deleted successfully")
                } else {
                    callback(false, it.exception?.message ?: "Delete failed")
                }
            }
    }

    override fun getWishlist(
        userId: String,
        callback: (List<WishlistBook>) -> Unit
    ) {
        val ref = FirebaseDatabase.getInstance().getReference("wishlist").child(userId)

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val wishlist = mutableListOf<WishlistBook>()
                for (child in snapshot.children) {
                    val book = child.getValue(WishlistBook::class.java)
                    book?.let { wishlist.add(it) }
                }
                callback(wishlist)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(emptyList())
            }
        })
    }

    // 1. Fixed getReviewCount (Implemented the TODO)
    override fun getReviewCount(userId: String, callback: (Int) -> Unit) {
        val ref = FirebaseDatabase.getInstance().getReference("reviews")
        ref.orderByChild("userId").equalTo(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    callback(snapshot.childrenCount.toInt())
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(0)
                }
            })
    }


    override fun getMyReviews(userId: String, callback: (List<ReviewModel>) -> Unit) {
        val ref = FirebaseDatabase.getInstance().getReference("reviews")

        ref.orderByChild("userId").equalTo(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val reviews = mutableListOf<ReviewModel>()
                    for (data in snapshot.children) {
                        val review = data.getValue(ReviewModel::class.java)
                        review?.let { reviews.add(it) }
                    }
                    callback(reviews)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(emptyList())
                }
            })
    }
    override fun removeFromWishlist(userId: String, bookId: String, callback: (Boolean) -> Unit) {
        val ref = FirebaseDatabase.getInstance().getReference("wishlist")
            .child(userId)
            .child(bookId)

        ref.removeValue().addOnCompleteListener {
            callback(it.isSuccessful)
        }
    }

    override fun getReadCount(userId: String, callback: (Int) -> Unit) {
        val ref = FirebaseDatabase.getInstance().getReference("read_books").child(userId)

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                callback(snapshot.childrenCount.toInt())
            }

            override fun onCancelled(error: DatabaseError) {
                callback(0)
            }
        })
    }
}