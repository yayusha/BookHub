package com.example.sem3project.repo

import com.example.sem3project.model.AdminModel
import com.example.sem3project.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class AdminRepoImpl : AdminRepo {

    private val userDb = FirebaseDatabase.getInstance().getReference("Users")

    override fun fetchAllUsers(callback: (List<UserModel>) -> Unit) {
        userDb.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = snapshot.children.mapNotNull { data ->
                    data.getValue(UserModel::class.java)?.copy(userId = data.key ?: "")
                }
                callback(users)
            }

            override fun onCancelled(error: DatabaseError) {
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

    override fun fetchAdminProfile(callback: (Boolean, AdminModel?, String) -> Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: run {
            callback(false, null, "Not logged in")
            return
        }

        val dbRef = FirebaseDatabase.getInstance().getReference("Users").child(uid)

        dbRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val admin = snapshot.getValue(AdminModel::class.java)
                callback(true, admin, "Profile loaded")
            } else {
                callback(false, null, "Data not found")
            }
        }.addOnFailureListener {
            callback(false, null, it.message ?: "Database error")
        }
    }

    override fun updateAdminProfile(
        newName: String,
        callback: (Boolean, String) -> Unit
    ) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val updates = mapOf<String, Any>(
            "firstName" to newName
        )

        FirebaseDatabase.getInstance().getReference("Users")
            .child(uid)
            .updateChildren(updates)
            .addOnSuccessListener {
                callback(true, "Profile updated successfully")
            }
            .addOnFailureListener {
                callback(false, it.message ?: "Update failed")
            }
    }
}
