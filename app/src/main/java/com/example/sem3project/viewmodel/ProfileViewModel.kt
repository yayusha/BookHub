package com.example.sem3project.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sem3project.model.ProfileModel
import com.example.sem3project.repo.ProfileRepo

class ProfileViewModel(val repo: ProfileRepo) : ViewModel() {

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
