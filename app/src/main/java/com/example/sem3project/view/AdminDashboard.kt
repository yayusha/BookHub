package com.example.sem3project.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sem3project.R
import com.example.sem3project.viewmodel.AdminViewModel
import com.example.sem3project.viewmodel.ReviewViewModel

class AdminDashboard : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DashBody()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashBody() {
    val context = LocalContext.current
    val activity = context as Activity

    val adminViewModel: AdminViewModel = viewModel()

    val reviewViewModel: ReviewViewModel = viewModel()

    data class NavItem(val label: String, val icon: Int)

    var selectedIndex by remember { mutableStateOf(0) }

    val listItems = listOf(
        NavItem(label = "Books", R.drawable.baseline_book_24),
        NavItem(label = "Reviews", R.drawable.baseline_star_rate_24),
        NavItem(label = "Users", R.drawable.baseline_notifications_24),
        NavItem(label = "Profile", R.drawable.baseline_person_24),

    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                listItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                painter = painterResource(item.icon),
                                contentDescription = item.label
                            )
                        },
                        label = { Text(item.label) },
                        onClick = { selectedIndex = index },
                        selected = selectedIndex == index
                    )
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (selectedIndex) {
                0 -> BookHomeScreen()
                1 -> ReviewScreen(viewModel = reviewViewModel, onMenuClick = {})

                2 -> UserScreen(adminViewModel = adminViewModel)
                3 -> AdminProfileScreen(viewModel = adminViewModel)
                else -> BookHomeScreen()
            }
        }
    }
}
