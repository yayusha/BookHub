package com.example.sem3project.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.sem3project.model.ReviewModel
import com.example.sem3project.viewmodel.ReviewViewModel
import com.example.sem3project.ui.theme.green20

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewScreen(
    viewModel: ReviewViewModel,
    onMenuClick: () -> Unit
) {
    val reviews by viewModel.reviews
    val isLoading by viewModel.isLoading
    val deleteStatus by viewModel.deleteStatus

    // Tab state: 0 = All Reviews, 1 = Reported
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("All Reviews", "Reported")

    // Logic to filter the list based on the active tab
    val displayList = remember(reviews, selectedTabIndex) {
        if (selectedTabIndex == 1) {
            reviews.filter { it.isReported == true }
        } else {
            reviews
        }
    }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var reviewToDelete by remember { mutableStateOf<ReviewModel?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(deleteStatus) {
        deleteStatus?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearDeleteStatus()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Review Management") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = green20,
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    ),
                    navigationIcon = {
                        IconButton(onClick = onMenuClick) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )

                // TabRow for Admin switching
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = green20,
                    contentColor = Color.White,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                            color = Color.White
                        )
                    }
                ) {
                    tabs.forEachIndexed { index, title ->
                        val count = if (index == 1) " (${reviews.count { it.isReported }})" else ""
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = { Text(text = "$title$count", color = Color.White) }
                        )
                    }
                }
            }
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
                        modifier = Modifier.align(Alignment.Center),
                        color = green20
                    )
                }
                displayList.isEmpty() -> {
                    Text(
                        text = if (selectedTabIndex == 1) "No reported reviews found." else "No reviews available.",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Fixed LazyColumn items block
                        items(displayList) { review ->
                            ReviewCard(
                                review = review,
                                onDeleteClick = {
                                    reviewToDelete = review
                                    showDeleteDialog = true
                                },
                                onResolveClick = {
                                    viewModel.resolveReview(review.id)
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showDeleteDialog && reviewToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Content?") },
            text = { Text("This will permanently remove the review for violation of guidelines.") },
            confirmButton = {
                TextButton(onClick = {
                    reviewToDelete?.let { viewModel.deleteReview(it.reviewId, it.bookId) }
                    showDeleteDialog = false
                    reviewToDelete = null
                }) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ReviewCard(
    review: ReviewModel,
    onDeleteClick: () -> Unit,
    onResolveClick: () -> Unit
) {
    val isResolved = !review.isReported

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = green20),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        review.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                    Text(
                        "on ${review.date}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.LightGray
                    )
                }

                Row {
                    IconButton(onClick = onResolveClick) {
                        Icon(
                            imageVector = if (isResolved) Icons.Filled.CheckCircle else Icons.Outlined.CheckCircle,
                            contentDescription = "Resolve",
                            tint = if (isResolved) Color(0xFF4CAF50) else Color.White
                        )
                    }
                    IconButton(onClick = onDeleteClick) {
                        Icon(Icons.Default.Delete, "Delete", tint = Color.White)
                    }
                }
            }

            if (review.isReported) {
                Surface(
                    color = Color.White.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Reported Content",
                            color = Color.Yellow,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }

            Text(
                text = "Rating: ${review.rating}/5",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )

            if (review.content.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    review.content,
                    color = Color.LightGray,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

    }
}

