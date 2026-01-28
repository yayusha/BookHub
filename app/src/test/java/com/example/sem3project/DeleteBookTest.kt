package com.example.sem3project

import com.example.sem3project.repo.BookRepo
import com.example.sem3project.viewmodel.BookViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class DeleteBookTest {

    @Test
    fun delete_book_success_test() {
        val repo = mock<BookRepo>()
        val viewModel = BookViewModel(repo)

        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String) -> Unit>(1)
            callback(true, "Book deleted")
            null
        }.`when`(repo).deleteBook(eq("-Ojjz-QAdHhe22yGICLI"), any())

        var successResult = false
        var messageResult = ""
        viewModel.deleteBook("-Ojjz-QAdHhe22yGICLI") { success, msg ->
            successResult = success
            messageResult = msg
        }

        assertTrue(successResult)
        assertEquals("Book deleted", messageResult)
        verify(repo).deleteBook(eq("-Ojjz-QAdHhe22yGICLI"), any())
    }
}