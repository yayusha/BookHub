package com.example.sem3project.repo

import com.example.sem3project.model.ProfileModel
import com.example.sem3project.model.ReviewModel
import com.example.sem3project.model.WishlistBook
import javax.security.auth.callback.Callback

interface ProfileRepo {
    fun addProfile(model: ProfileModel,callback: (Boolean, String)-> Unit)
    fun getProfileById(
        userId : String,
        callback: (Boolean, String, ProfileModel? ) -> Unit
    )
    fun updateProfile(model: ProfileModel, callback: (Boolean, String) -> Unit)
    fun deleteProfile(userId: String, callback: (Boolean, String) -> Unit)

    fun getWishlist(userId: String, callback: (List<WishlistBook>) -> Unit)
    fun getReviewCount(userId: String, callback: (Int) -> Unit)
    fun removeFromWishlist(userId: String, bookId: String, callback: (Boolean) -> Unit)
    fun getReadCount(userId: String, callback: (Int) -> Unit)
    fun getMyReviews(userId: String, callback: (List<ReviewModel>) -> Unit)
}