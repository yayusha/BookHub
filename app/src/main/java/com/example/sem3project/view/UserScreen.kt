package com.example.sem3project.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sem3project.model.UserModel
import com.example.sem3project.viewmodel.AdminViewModel

@Composable
fun UserScreen(adminViewModel: AdminViewModel = viewModel()) {

    val users by adminViewModel.usersList
    val actionStatus by adminViewModel.actionStatus

    // Fetch users once when screen loads
    LaunchedEffect(Unit) {
        adminViewModel.fetchUsers()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Registered Users", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        // Show action status if any
        actionStatus?.let { (success, msg) ->
            Text(
                text = msg,
                color = if (success) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (users.isEmpty()) {
            Text("No users found", style = MaterialTheme.typography.bodyMedium)
        } else {

            // Filter the list to exclude anyone with the "admin" role
            val filteredUsers = users.filter {
                !it.role.equals("admin", ignoreCase = true)
            }
            LazyColumn {
                items(filteredUsers) { user ->
                    UserItem(user = user, adminViewModel = adminViewModel)
                }
            }
        }
    }
}

@Composable
fun UserItem(user: UserModel, adminViewModel: AdminViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = "Email: ${user.email ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Name: ${user.firstName ?: ""} ${user.lastName ?: ""}", style = MaterialTheme.typography.bodySmall)
                Text(text = "Status: ${if (user.isBanned) "Banned" else "Active"}", style = MaterialTheme.typography.bodySmall)
            }

            Button(
                onClick = {
                    if (user.isBanned) {
                        adminViewModel.unbanUser(user.userId ?: "")
                    } else {
                        adminViewModel.banUser(user.userId ?: "")
                    }
                }
            ) {
                Text(if (user.isBanned) "Unban" else "Ban")
            }
        }
    }
}
