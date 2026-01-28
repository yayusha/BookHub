package com.example.sem3project.repo

import com.example.sem3project.model.UserModel
import com.google.firebase.database.*

class AdminRepoImpl : AdminRepo {

    private val userDb = FirebaseDatabase.getInstance().getReference("Users") // or your users path

    override fun fetchAllUsers(callback: (List<UserModel>) -> Unit) {
        userDb.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = snapshot.children.mapNotNull { data ->
                    data.getValue(UserModel::class.java)?.copy(userId = data.key ?: "")
                }
                callback(users)
            }

            override fun onCancelled(error: DatabaseError) {
                // If the user is deleted or permission is lost, this triggers
                callback(emptyList())
            }
        })
    }

    override fun banUser(userId: String, callback: (Boolean, String) -> Unit) {
        userDb.child(userId).child("status").setValue("banned")
            .addOnSuccessListener { callback(true, "User banned") }
            .addOnFailureListener { callback(false, "Failed to ban user") }
    }

    override fun unbanUser(userId: String, callback: (Boolean, String) -> Unit) {
        userDb.child(userId).child("status").setValue("active")
            .addOnSuccessListener { callback(true, "User unbanned") }
            .addOnFailureListener { callback(false, "Failed to unban user") }
    }
}
