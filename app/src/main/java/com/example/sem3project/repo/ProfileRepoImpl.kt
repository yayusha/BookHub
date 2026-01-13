package com.example.sem3project.repo

import com.example.sem3project.model.ProfileModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileRepoImpl : ProfileRepo {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val ref: DatabaseReference = database.getReference("Profiles")

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
}