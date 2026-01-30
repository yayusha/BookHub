package com.example.sem3project.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sem3project.viewmodel.ReviewViewModel

@Composable
fun NotificationScreen(
    reviewViewModel: ReviewViewModel = viewModel(),
    currentUserId: String
) {
    val notifications by reviewViewModel.notifications

    LaunchedEffect(currentUserId) {
        reviewViewModel.fetchNotifications(currentUserId)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        if (notifications.isEmpty()) {
            item { Text("No notifications", color = Color.Gray) }
        } else {
            items(notifications) { notif ->
                val message = when(notif["type"]?.toString()) {
                    "like" -> "liked your review"
                    "reply" -> "replied to your review"
                    else -> "interacted with your post"
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(notif["type"]?.toString() ?: "Notification", fontWeight = FontWeight.Bold)
                        Text("From: ${notif["fromUserId"]}", fontSize = 12.sp, color = Color.Gray)
                        Text("Date: ${notif["date"]}", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }
        }
    }
}
