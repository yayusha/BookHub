package com.example.sem3project.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sem3project.model.UserModel
import com.example.sem3project.viewmodel.AdminViewModel
import com.example.sem3project.viewmodel.ReviewViewModel

@Composable
fun UserScreen(adminViewModel: AdminViewModel) {

    val searchQuery by adminViewModel.searchQuery
    val users = adminViewModel.filteredUsers
    val actionStatus by adminViewModel.actionStatus

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Registered Users", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        //SEARCH BAR UI
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { adminViewModel.searchQuery.value = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search by name or email...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { adminViewModel.searchQuery.value = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear")
                    }
                }
            },
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        actionStatus?.let { (success, msg) ->
            Text(
                text = msg,
                color = if (success) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (users.isEmpty()) {
            Text(
                text = if (searchQuery.isEmpty()) "No users found" else "No results for '$searchQuery'",
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            val nonAdminUsers = users.filter { !it.role.equals("admin", ignoreCase = true) }

            LazyColumn {
                items(nonAdminUsers) { user ->
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
