package com.example.sem3project.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sem3project.model.ProfileModel
import com.example.sem3project.model.ReviewModel
import com.example.sem3project.model.WishlistBook
import com.example.sem3project.repo.ProfileRepo

class ProfileViewModel(val repo: ProfileRepo) : ViewModel() {

    private val _wishlist = mutableStateOf<List<WishlistBook>>(emptyList())
    val wishlist: State<List<WishlistBook>> = _wishlist

    private val _reviewCount = mutableStateOf(0)
    val reviewCount: State<Int> = _reviewCount

    private val _readCount = mutableStateOf(0)
    val readCount: State<Int> = _readCount

    private val _myReviews = mutableStateOf<List<ReviewModel>>(emptyList())
    val myReviews: State<List<ReviewModel>> = _myReviews
    fun loadUserData(userId: String) {
        if (userId.isEmpty()) return

        // Fetch Wishlist
        repo.getWishlist(userId) { list ->
            _wishlist.value = list
        }

        //  Fetch the actual reviews list
        repo.getMyReviews(userId) { reviews ->
            _myReviews.value = reviews
        }

        // Fetch Review Count
        repo.getReviewCount(userId) { count ->
            _reviewCount.value = count
        }

        repo.getReadCount(userId) { _readCount.value = it }

        getProfileById(userId)
    }

    fun addProfile(model: ProfileModel, callback: (Boolean, String) -> Unit) {
        repo.addProfile(model, callback)
    }

    fun updateProfile(model: ProfileModel, callback: (Boolean, String) -> Unit) {
        repo.updateProfile(model, callback)
    }

    fun deleteProfile(userId: String, callback: (Boolean, String) -> Unit) {
        repo.deleteProfile(userId, callback)
    }


    fun deactivateProfile(userId: String) {
        repo.deleteProfile(userId) { _, _ -> }
    }

    fun removeFromWishlist(userId: String, bookId: String) {
        repo.removeFromWishlist(userId, bookId) { success ->
            if (success) {

            }
        }
    }

    private val _profile = MutableLiveData<ProfileModel?>()
    val profile: MutableLiveData<ProfileModel?> get() = _profile

    fun getProfileById(userId: String) {
        repo.getProfileById(userId) { success, message, data ->
            if (success) {
                _profile.postValue(data)
            }
        }
    }
}
