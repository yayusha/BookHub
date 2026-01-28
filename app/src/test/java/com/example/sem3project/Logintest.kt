package com.example.sem3project


import com.example.sem3project.repo.authrepo
import com.example.sem3project.viewmodel.AuthViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify


class Logintest {

    @Test
    fun login_success_test() {
        val repo = mock<authrepo>()
        val viewModel = AuthViewModel(repo)

        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String) -> Unit>(2)
            callback(true, "Login success")
            null
        }.`when`(repo).login(eq("ram@gmail.com"), eq("Ram12345"), any())

        var successResult = false
        var messageResult = ""

        viewModel.login("ram@gmail.com", "Ram12345") { success, msg ->
            successResult = success
            messageResult = msg
        }

        assertTrue(successResult)
        assertEquals("Login success", messageResult)

        verify(repo).login(eq("ram@gmail.com"), eq("Ram12345"), any())
    }

}