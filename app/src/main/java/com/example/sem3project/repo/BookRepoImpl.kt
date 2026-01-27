package com.example.sem3project.repo

import com.example.sem3project.model.BookModel
import com.google.firebase.database.*

class BookRepoImpl : BookRepo {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val ref: DatabaseReference = database.getReference("Books")

    // 1️⃣ Add a book
    override fun addBook(model: BookModel, callback: (Boolean, String) -> Unit) {
        val id = ref.push().key.toString()
        model.bookId = id
        ref.child(id).setValue(model).addOnCompleteListener {
            if (it.isSuccessful) callback(true, "Book added successfully")
            else callback(false, it.exception?.message ?: "Failed to add book")
        }
    }

    // 2️⃣ Get all books
    override fun getAllBooks(callback: (Boolean, String, List<BookModel>) -> Unit) {
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val books = mutableListOf<BookModel>()
                for (data in snapshot.children) {
                    data.getValue(BookModel::class.java)?.let { books.add(it) }
                }
                callback(true, "Books fetched", books)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message, emptyList())
            }
        })
    }

    // 3️⃣ Get book by ID
    override fun getBookById(bookID: String, callback: (Boolean, String, BookModel?) -> Unit) {
        ref.child(bookID).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val book = snapshot.getValue(BookModel::class.java)
                if (book != null) callback(true, "Book fetched", book)
                else callback(false, "Book not found", null)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message, null)
            }
        })
    }

    // 4️⃣ Update book
    override fun updateBook(model: BookModel, callback: (Boolean, String) -> Unit) {
        ref.child(model.bookId).updateChildren(model.toMap()).addOnCompleteListener {
            if (it.isSuccessful) callback(true, "Book updated successfully")
            else callback(false, it.exception?.message ?: "Failed to update book")
        }
    }

    // 5️⃣ Delete book
    override fun deleteBook(bookID: String, callback: (Boolean, String) -> Unit) {
        ref.child(bookID).removeValue().addOnCompleteListener {
            if (it.isSuccessful) callback(true, "Book deleted successfully")
            else callback(false, it.exception?.message ?: "Failed to delete book")
        }
    }

    // 6️⃣ Get books by genre
    override fun getBookByGenre(genreId: String, callback: (Boolean, String, List<BookModel>?) -> Unit) {
        ref.orderByChild("genreId").equalTo(genreId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val books = mutableListOf<BookModel>()
                    for (data in snapshot.children) {
                        data.getValue(BookModel::class.java)?.let { books.add(it) }
                    }
                    callback(true, "Books fetched by genre", books)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(false, error.message, emptyList())
                }
            })
    }
}
