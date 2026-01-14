package com.example.sem3project.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.sem3project.repo.ImageRepo
import com.example.sem3project.repo.ImageRepoImpl



class ImageViewModel(val repo: ImageRepo
) : ViewModel() {
    fun uploadImage(context: Context, imageUri: Uri, callback: (Boolean, String?) -> Unit) {
        repo.uploadImage(context, imageUri, callback)
    }
}