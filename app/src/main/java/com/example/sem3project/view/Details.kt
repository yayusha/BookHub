package com.example.sem3project.view

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.sem3project.R
import com.example.sem3project.model.ReviewModel
import com.example.sem3project.repo.BookRepoImpl
import com.example.sem3project.ui.theme.White20
import com.example.sem3project.viewmodel.BookViewModel
import com.example.sem3project.viewmodel.BookViewModelFactory
import com.example.sem3project.viewmodel.ReviewViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailsScreen(
    bookId: String,
    navController: NavController,
    bookViewModel: BookViewModel = viewModel(
        factory = BookViewModelFactory(BookRepoImpl())
    ),
    reviewViewModel: ReviewViewModel = viewModel()
) {
    val context = LocalContext.current
    val activity = context as? Activity

    val selectedBook by bookViewModel.selectedBook.observeAsState()
    val reviewList by reviewViewModel.reviews

    var showReviewDialog by remember { mutableStateOf(false) }
    var reviewTitle by remember { mutableStateOf("") }
    var reviewContent by remember { mutableStateOf("") }
    var reviewRating by remember { mutableStateOf(5) }
    var selectedTab by remember { mutableStateOf("Summary") }

    val hubGreen = colorResource(R.color.Hub)

    LaunchedEffect(bookId) {
        bookViewModel.getBookById(bookId)
        reviewViewModel.fetchBookSpecificReviews(bookId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Book Details", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = {
                        // Check if there is a screen to go back to
                        if (navController.previousBackStackEntry != null) {
                            navController.popBackStack()
                        } else {
                            // If no backstack (rare in this setup), then you can finish the activity
                            activity?.finish()
                        }
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { /* Favorite Logic */ }) {
                        Icon(Icons.Default.Favorite, contentDescription = "Favorite", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = hubGreen,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            if (selectedTab == "Reviews") {
                ExtendedFloatingActionButton(
                    onClick = { showReviewDialog = true },
                    containerColor = hubGreen,
                    contentColor = Color.White,
                    icon = { Icon(Icons.Default.Star, contentDescription = null) },
                    text = { Text("Rate Book") }
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF8F9FA)) // Subtle off-white background
        ) {
            // --- BOOK HEADER SECTION ---
            item {
                selectedBook?.let { book ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(hubGreen.copy(alpha = 0.05f)) // Light tinted header
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Card(
                            elevation = CardDefaults.cardElevation(8.dp),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.width(160.dp).height(240.dp)
                        ) {
                            AsyncImage(
                                model = book.imageUrl,
                                contentDescription = book.bookName,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Text(book.bookName, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center)
                        Text("By ${book.author}", fontSize = 16.sp, color = hubGreen, fontWeight = FontWeight.Medium)
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color.LightGray.copy(alpha = 0.3f),
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text(book.genreId, modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp), fontSize = 12.sp)
                        }
                    }
                }
            }

            // --- NAVIGATION TABS ---
            item {
                TabRow(
                    selectedTabIndex = if (selectedTab == "Summary") 0 else 1,
                    containerColor = Color.White,
                    contentColor = hubGreen,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            Modifier.tabIndicatorOffset(tabPositions[if (selectedTab == "Summary") 0 else 1]),
                            color = hubGreen
                        )
                    }
                ) {
                    Tab(
                        selected = selectedTab == "Summary",
                        onClick = { selectedTab = "Summary" },
                        text = { Text("Summary", fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = selectedTab == "Reviews",
                        onClick = { selectedTab = "Reviews" },
                        text = { Text("Reviews (${reviewList.size})", fontWeight = FontWeight.Bold) }
                    )
                }
            }

            // --- CONTENT SECTION ---
            if (selectedTab == "Summary") {
                item {
                    Text(
                        text = selectedBook?.summary ?: "No summary available.",
                        modifier = Modifier.padding(20.dp),
                        lineHeight = 24.sp,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                if (reviewList.isEmpty()) {
                    item {
                        Box(Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                            Text("No reviews yet. Be the first to rate!", color = Color.Gray)
                        }
                    }
                } else {
                    items(reviewList) { review ->
                        ReviewItemUi(review)
                    }
                }
                // Extra padding for FAB
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }

    if (showReviewDialog) {
        ReviewSubmissionDialog(
            title = reviewTitle,
            onTitleChange = { reviewTitle = it },
            content = reviewContent,
            onContentChange = { reviewContent = it },
            rating = reviewRating,
            onRatingChange = { reviewRating = it },
            onDismiss = { showReviewDialog = false },
            onSubmit = {
                selectedBook?.let { book ->
                    val newReview = ReviewModel(
                        bookId = book.bookId,
                        date = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date()),
                        title = reviewTitle,
                        content = reviewContent,
                        rating = reviewRating.toDouble()
                    )
                    reviewViewModel.addReview(newReview) { success ->
                        if (success) {
                            showReviewDialog = false
                            reviewTitle = ""
                            reviewContent = ""
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun ReviewItemUi(review: ReviewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // User Initial Circle
                Surface(
                    shape = CircleShape,
                    color = colorResource(R.color.Hub).copy(alpha = 0.1f),
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(review.title.take(1).uppercase(), fontWeight = FontWeight.Bold, color = colorResource(R.color.Hub))
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(review.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    Text(review.date, fontSize = 11.sp, color = Color.Gray)
                }
                Spacer(modifier = Modifier.weight(1f))
                // Small Rating Badge
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("${review.rating.toInt()}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Icon(Icons.Default.Star, null, tint = Color(0xFFFFC107), modifier = Modifier.size(16.dp))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(review.content, style = MaterialTheme.typography.bodyMedium, color = Color.DarkGray)
        }
    }
}

@Composable
fun ReviewSubmissionDialog(
    title: String, onTitleChange: (String) -> Unit,
    content: String, onContentChange: (String) -> Unit,
    rating: Int, onRatingChange: (Int) -> Unit,
    onDismiss: () -> Unit,
    onSubmit: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Write a Review", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = onTitleChange,
                    label = { Text("Headline (e.g. Amazing Book!)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = content,
                    onValueChange = onContentChange,
                    label = { Text("Review Details") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
                Column {
                    Text("Your Rating", style = MaterialTheme.typography.labelMedium)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(5) { index ->
                            val starIndex = index + 1
                            IconButton(onClick = { onRatingChange(starIndex) }) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = if (starIndex <= rating) Color(0xFFFFC107) else Color.LightGray,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onSubmit, colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.Hub))) {
                Text("Submit Review")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = Color.Gray) }
        }
    )
}