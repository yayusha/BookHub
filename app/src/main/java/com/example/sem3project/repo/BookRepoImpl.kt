package com.example.sem3project.repo

import com.example.sem3project.model.BookModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.jvm.java


class BookRepoImpl: BookRepo {
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()

    val ref: DatabaseReference = database.getReference("Books")

    override fun addBook(
        model: BookModel,
        callback: (Boolean, String) -> Unit
    ) {
        var id = ref.push().key.toString()

        model.bookId = id

        ref.child(id).setValue(model).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Book add successfully")
            } else {
                callback(false, "${it.exception?.message}")
            }
        }

    }

    override fun getAllBooks
                (callback: (Boolean, String, List<BookModel>) -> Unit)
    {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var allBook = mutableListOf<BookModel>()
                    for (data in snapshot.children) {
                        var book = data.getValue(BookModel::class.java)
                        if (book != null) {
                            allBook.add(book)
                        }
                    }
                    callback(true, "Book fetched", allBook)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message, emptyList())
            }
        })
    }


    override fun getBookById(
        bookID: String,
        callback: (Boolean, String, BookModel?) -> Unit
    ) {
        ref.child(bookID).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var data = snapshot.getValue(BookModel::class.java)
                    if (data != null) {
                        callback(true, "Book fetched", data)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message, null)
            }
        })

    }

    override fun updateBook(
        model: BookModel,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(model.bookId).updateChildren(model.toMap()).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Book updated successfully")
            } else {
                callback(false, "${it.exception?.message}")
            }
        }

    }

    override fun deleteBook(
        bookID: String,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(bookID).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Book deleted successfully")
            } else {
                callback(false, "${it.exception?.message}")
            }
        }

    }

    override fun getBookByGenre(
        genreId: String,
        callback: (Boolean, String, List<BookModel>?) -> Unit
    ) {
        ref.orderByChild("genreId").equalTo(genreId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        var allBooks = mutableListOf<BookModel>()
                        for (data in snapshot.children) {
                            var book = data.getValue(BookModel::class.java)
                            if (book != null) {
                                allBooks.add(book)
                            }
                        }
                        callback(true, "Books fetched", allBooks)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(false, error.message, emptyList())
                }
            })
    }

}
