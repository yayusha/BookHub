package com.example.sem3project

import com.example.sem3project.model.ReviewModel
import com.example.sem3project.repo.ReviewRepo
import com.example.sem3project.viewmodel.ReviewViewModel
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class AddReviewTest {

    @Test
    fun add_review_success_test() {

        val repo = mock<ReviewRepo>()
        val viewModel = ReviewViewModel(repo)

        val testReview = ReviewModel(
            id = "review123",
            date = "2024-01-28",
            title = "Great Book",
            content = "This book was amazing! Highly recommended.",
            rating = 5.0
        )

        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean) -> Unit>(1)
            callback(true)
            null
        }.`when`(repo).addReview(eq(testReview), any())

        var successResult = false

        viewModel.addReview(testReview) { success ->
            successResult = success
        }
        assertTrue(successResult)
        verify(repo).addReview(eq(testReview), any())
    }
}