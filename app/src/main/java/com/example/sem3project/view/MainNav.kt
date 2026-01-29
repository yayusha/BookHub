package com.example.sem3project.view

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

//@Composable
//fun MainNav() {
//    val navController = rememberNavController()
//
//    NavHost(navController = navController, startDestination = "home") {
//        composable("home") {
//            HomeScreen(navController)
//        }
//        composable("detail/{bookId}") { backStackEntry ->
//            val bookId = backStackEntry.arguments?.getString("bookId") ?: ""
//            BookDetailsScreen(bookId = bookId)
//        }
//    }
//}

@Composable
fun MainNav() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController)
        }
        composable("detail/{bookId}") { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId") ?: ""

            BookDetailsScreen(
                bookId = bookId,
                navController = navController
            )
        }
    }
}
