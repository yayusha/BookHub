package com.example.sem3project.repo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class authrepoimpl: authrepo {
     val auth: FirebaseAuth = FirebaseAuth.getInstance()
     val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val ref = database.getReference("Users")

    override fun login(
        email: String,
        password: String,
        callback: (Boolean, String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) callback(true, "Login success")
            else callback(false, it.exception?.message ?: "Login failed")
        }    }

    override fun register(
        email: String,
        password: String,
        callback: (Boolean, String, String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    callback(
                        true,
                        "Registration successful",
                        auth.currentUser?.uid ?: ""
                    )
                } else {
                    callback(false, it.exception?.message ?: "Registration failed", "")
                }
            }    }

    override fun forgotPassword(
        email: String,
        callback: (Boolean, String) -> Unit
    ) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener {
            if (it.isSuccessful) callback(true, "Reset link sent")
            else callback(false, it.exception?.message ?: "Failed")
        }    }
}