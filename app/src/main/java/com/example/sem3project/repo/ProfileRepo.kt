package com.example.sem3project.repo

import com.example.sem3project.model.ProfileModel
import javax.security.auth.callback.Callback

interface ProfileRepo {
    fun addProfile(model: ProfileModel,callback: (Boolean, String)-> Unit)
    fun getProfileById(
        userId : String,
        callback: (Boolean, String, ProfileModel? ) -> Unit
    )
    fun updateProfile(model: ProfileModel, callback: (Boolean, String) -> Unit)
    fun deleteProfile(userId: String, callback: (Boolean, String) -> Unit)
}