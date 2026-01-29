package com.example.sem3project.view

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.sem3project.R
import com.example.sem3project.model.ReviewModel
import com.example.sem3project.repo.BookRepoImpl
import com.example.sem3project.viewmodel.BookViewModel
import com.example.sem3project.viewmodel.BookViewModelFactory
import com.example.sem3project.viewmodel.ReviewViewModel
import org.chromium.base.Flag
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailsScreen(
    bookId: String,
    navController: NavController,
    currentUserId: String = "abc123", // Replace with your Auth userId
    bookViewModel: BookViewModel = viewModel(
        factory = BookViewModelFactory(BookRepoImpl())
    ),
    reviewViewModel: ReviewViewModel = viewModel()
) {
    val context = LocalContext.current
    val activity = context as? Activity

    val selectedBook by bookViewModel.selectedBook.observeAsState()
    val reviewList by reviewViewModel.reviews

    // --- UI States ---
    var selectedTab by remember { mutableStateOf("Summary") }
    var showReviewDialog by remember { mutableStateOf(false) }
    var reviewTitle by remember { mutableStateOf("") }
    var reviewContent by remember { mutableStateOf("") }
    var reviewRating by remember { mutableStateOf(5) }

    var reviewToEdit by remember { mutableStateOf<ReviewModel?>(null) }
    var reviewToDelete by remember { mutableStateOf<ReviewModel?>(null) }
    var reviewToReport by remember { mutableStateOf<ReviewModel?>(null) }

    val hubGreen = colorResource(R.color.Hub)

    LaunchedEffect(bookId) {
        bookViewModel.getBookById(bookId)
        reviewViewModel.fetchBookSpecificReviews(bookId)
    }

    // --- Scaffold ---
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Book Details", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = {
                        if (navController.previousBackStackEntry != null) navController.popBackStack()
                        else activity?.finish()
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
                .background(Color(0xFFF8F9FA))
        ) {
            // --- BOOK HEADER ---
            item {
                selectedBook?.let { book ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(hubGreen.copy(alpha = 0.05f))
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
                        Text(book.bookName, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                        Text("By ${book.author}", fontSize = 16.sp, color = hubGreen)
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color.LightGray.copy(alpha = 0.3f),
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text(book.genreId, modifier = Modifier.padding(8.dp), fontSize = 12.sp)
                        }
                    }
                }
            }

            // --- TABS ---
            item {
                TabRow(
                    selectedTabIndex = if (selectedTab == "Summary") 0 else 1,
                    containerColor = Color.White,
                    contentColor = hubGreen
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

            // --- SUMMARY OR REVIEWS CONTENT ---
            if (selectedTab == "Summary") {
                item {
                    Text(
                        text = selectedBook?.summary ?: "No summary available.",
                        modifier = Modifier.padding(20.dp),
                        lineHeight = 24.sp
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
                        ReviewItemUi(
                            review = review,
                            isOwnReview = review.userId == currentUserId,
                            onEditClick = { reviewToEdit = it },
                            onDeleteClick = { reviewToDelete = it },
                            onReportClick = { reviewToReport = it }
                        )
                    }
                }
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }

    // --- ADD / EDIT REVIEW DIALOG INLINE ---
    if (showReviewDialog || reviewToEdit != null) {
        val editing = reviewToEdit != null
        val review = reviewToEdit

        var title by remember { mutableStateOf(review?.title ?: reviewTitle) }
        var content by remember { mutableStateOf(review?.content ?: reviewContent) }
        var rating by remember { mutableStateOf(review?.rating?.toInt() ?: reviewRating) }

        AlertDialog(
            onDismissRequest = {
                showReviewDialog = false
                reviewToEdit = null
            },
            title = { Text(if (editing) "Edit Review" else "Add Review") },
            text = {
                Column {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Title") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = content,
                        onValueChange = { content = it },
                        label = { Text("Content") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Rating: ", fontWeight = FontWeight.Bold)
                        for (i in 1..5) {
                            IconButton(onClick = { rating = i }) {
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = null,
                                    tint = if (i <= rating) Color(0xFFFFD700) else Color.Gray
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    val newReview = ReviewModel(
                        bookId = selectedBook?.bookId ?: "",
                        date = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date()),
                        title = title,
                        content = content,
                        rating = rating.toDouble(),
                        userId = currentUserId,
                        reviewId = review?.reviewId ?: ""
                    )
                    if (editing) {
                        reviewViewModel.saveOrUpdateReview(newReview, isEdit = true,
                            bookId = bookId) { success ->
                            if (success) reviewToEdit = null
                        }
                    }
                    else {
                        // PASS bookId here
                        reviewViewModel.addReview(newReview, selectedBook?.bookId ?: "") {
                            showReviewDialog = false
                        }
                    }
                }) {
                    Text("Submit")
                }

            },
            dismissButton = {
                TextButton(onClick = {
                    showReviewDialog = false
                    reviewToEdit = null
                }) {
                    Text("Cancel")
                }
            }
        )
    }

    // --- DELETE REVIEW DIALOG ---
    if (reviewToDelete != null) {
        AlertDialog(
            onDismissRequest = { reviewToDelete = null },
            title = { Text("Delete Review") },
            text = { Text("Are you sure you want to delete this review?") },
            confirmButton = {
                Button(onClick = {
                    reviewToDelete?.let { reviewViewModel.deleteReview(it.reviewId,bookId) }
                    reviewToDelete = null
                }) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { reviewToDelete = null }) { Text("Cancel") }
            }
        )
    }

    // --- REPORT REVIEW DIALOG ---
    if (reviewToReport != null) {
        AlertDialog(
            onDismissRequest = { reviewToReport = null },
            title = { Text("Report Review") },
            text = { Text("Do you want to report this review?") },
            confirmButton = {
                Button(onClick = {
                    reviewToReport?.let { reviewViewModel.resolveReview(it.reviewId) }
                    reviewToReport = null
                }) { Text("Report") }
            },
            dismissButton = {
                TextButton(onClick = { reviewToReport = null }) { Text("Cancel") }
            }
        )
    }
}


// --- REVIEW ITEM ---
@Composable
fun ReviewItemUi(
    review: ReviewModel,
    isOwnReview: Boolean,
    onEditClick: (ReviewModel) -> Unit,
    onDeleteClick: (ReviewModel) -> Unit,
    onReportClick: (ReviewModel) -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(review.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(review.rating.toInt()) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFD700), modifier = Modifier.size(18.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(review.content, fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                if (isOwnReview) {
                    TextButton(onClick = { onEditClick(review) }) { Text("Edit") }
                    TextButton(onClick = { onDeleteClick(review) }) { Text("Delete") }
                } else {
                    // Only show report if it’s NOT the user’s own review
                    TextButton(onClick = { onReportClick(review) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_flag_2_24),
                            contentDescription = "Report Review",
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Report")
                    }
                }
            }
        }
    }
}
