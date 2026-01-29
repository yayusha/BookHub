package com.example.sem3project.view

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.example.sem3project.view.BookDetailsScreen
import com.example.sem3project.view.HomeScreen


@Composable
fun MainNav() {
    val navController = rememberNavController()
    val auth = FirebaseAuth.getInstance()
    val currentUid = auth.currentUser?.uid ?: ""

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController)
        }
        composable("detail/{bookId}") { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId") ?: ""

            BookDetailsScreen(
                bookId = bookId,
                navController = navController,
                currentUserId = currentUid
            )
        }
        composable("notifications") {
            NotificationScreen(
                currentUserId = currentUid
            )
        }
    }
}
