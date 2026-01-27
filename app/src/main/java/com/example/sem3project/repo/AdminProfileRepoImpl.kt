package com.example.sem3project.repo

import com.example.sem3project.model.AdminModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AdminProfileRepoImpl : AdminProfileRepo {

    private val auth = FirebaseAuth.getInstance()
    private val dbRef = FirebaseDatabase.getInstance().reference

    override fun fetchAdminProfile(
        callback: (Boolean, AdminModel?, String) -> Unit
    ) {
        val uid = auth.currentUser?.uid ?: run {
            callback(false, null, "Admin not logged in")
            return
        }

        dbRef.child("admins").child(uid)
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val admin = snapshot.getValue(AdminModel::class.java)
                    callback(true, admin, "Profile loaded")
                } else {
                    callback(false, null, "Admin profile not found")
                }
            }
            .addOnFailureListener {
                callback(false, null, it.message ?: "Failed to load profile")
            }
    }

    override fun updateAdminProfile(
        name: String,
        username: String,
        callback: (Boolean, String) -> Unit
    ) {
        val uid = auth.currentUser?.uid ?: run {
            callback(false, "Admin not logged in")
            return
        }

        val updates = mapOf<String, Any>(
            "name" to name,
            "username" to username
        )

        dbRef.child("admins").child(uid)
            .updateChildren(updates)
            .addOnSuccessListener {
                callback(true, "Profile updated successfully")
            }
            .addOnFailureListener {
                callback(false, it.message ?: "Profile update failed")
            }
    }
}
