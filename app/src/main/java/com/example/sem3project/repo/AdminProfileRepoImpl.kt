package com.example.sem3project.repo

import com.example.sem3project.model.AdminModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AdminProfileRepoImpl : AdminProfileRepo {

    private val auth = FirebaseAuth.getInstance()
    private val dbRef = FirebaseDatabase.getInstance().reference

    override fun fetchAdminProfile(callback: (Boolean, AdminModel?, String) -> Unit) {
        val uid = auth.currentUser?.uid ?: run {
            callback(false, null, "No session found")
            return
        }

        dbRef.child("Users").child(uid).get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val admin = snapshot.getValue(AdminModel::class.java)
                callback(true, admin, "Success")
            } else {
                callback(false, null, "Profile not found")
            }
        }.addOnFailureListener {
            callback(false, null, it.message ?: "Database error")
        }
    }

    override fun updateAdminProfile(
        name: String,
        callback: (Boolean, String) -> Unit
    ) {
        val uid = auth.currentUser?.uid ?: run {
            callback(false, "Admin not logged in")
            return
        }

        val updates = mapOf<String, Any>(
            "firstName" to name,
            "role" to "admin"
        )

        dbRef.child("Users").child(uid).updateChildren(updates)
            .addOnSuccessListener {
                callback(true, "Profile updated successfully")
            }
            .addOnFailureListener {
                callback(false, it.message ?: "Update failed")
            }
    }


}
