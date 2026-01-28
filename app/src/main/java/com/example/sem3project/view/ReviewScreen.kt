package com.example.sem3project.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.sem3project.model.ReviewModel
import com.example.sem3project.viewmodel.ReviewViewModel
import com.example.sem3project.ui.theme.green20
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
// admin review screen
fun ReviewScreen(
    viewModel: ReviewViewModel,
    onMenuClick: () -> Unit
) {
    val reviews by viewModel.reviews
    val isLoading by viewModel.isLoading
    val deleteStatus by viewModel.deleteStatus

    var showDeleteDialog by remember { mutableStateOf(false) }
    var reviewToDelete by remember { mutableStateOf<ReviewModel?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    // Show snackbar when delete status changes
    LaunchedEffect(deleteStatus) {
        deleteStatus?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearDeleteStatus()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("All Reviews") },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                reviews.isEmpty() -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No reviews found",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Reviews will appear here once users add them",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Text(
                                text = "${reviews.size} Reviews",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        items(reviews) { review ->
                            ReviewCard(
                                review = review,
                                onDeleteClick = {
                                    reviewToDelete = review
                                    showDeleteDialog = true
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // Delete confirmation dialog
    if (showDeleteDialog && reviewToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Review") },
            text = {
                Text("Are you sure you want to delete this review? This action cannot be undone.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        reviewToDelete?.let { viewModel.deleteReview(it.id) }
                        showDeleteDialog = false
                        reviewToDelete = null
                    }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    reviewToDelete = null
                }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ReviewCard(
    review: ReviewModel,
    onDeleteClick: () -> Unit
) {
    // Local state to track if review is resolved (frontend only)
    var isResolved by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = green20
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header with book title and action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = review.title,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                        // Show "Resolved" badge when marked as resolved
                        if (isResolved) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                color = Color(0xFF4CAF50),
                                shape = MaterialTheme.shapes.small
                            ) {
                                Text(
                                    text = "Resolved",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "on ${review.date}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.LightGray
                    )
                }


                Row {
                    // Resolve/Unresolve button
                    IconButton(
                        onClick = {
                            isResolved = !isResolved
                        }
                    ) {
                        Icon(
                            imageVector = if (isResolved) Icons.Filled.CheckCircle else Icons.Outlined.CheckCircle,
                            contentDescription = if (isResolved) "Mark as unresolved" else "Mark as resolved",
                            tint = if (isResolved) Color(0xFF4CAF50) else Color.White
                        )
                    }

                    // Delete button
                    IconButton(onClick = onDeleteClick) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete review",
                            tint = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Rating
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "⭐",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = String.format("%.1f/5.0", review.rating),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Comment
            if (review.content.isNotEmpty()) {
                Text(
                    text = review.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.LightGray
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}