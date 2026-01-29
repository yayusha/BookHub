package com.example.sem3project.view

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.example.sem3project.model.ReplyModel
import com.example.sem3project.model.ReviewModel
import com.example.sem3project.repo.BookRepoImpl
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
    currentUserId: String = "abc123", // Replace with real Auth UID
    bookViewModel: BookViewModel = viewModel(
        factory = BookViewModelFactory(BookRepoImpl())
    ),
    reviewViewModel: ReviewViewModel = viewModel()
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val hubGreen = colorResource(R.color.Hub)

    val selectedBook by bookViewModel.selectedBook.observeAsState()
    val reviewList by reviewViewModel.reviews

    // --- State Management ---
    var selectedTab by remember { mutableStateOf("Summary") }
    var showReviewDialog by remember { mutableStateOf(false) }

    // Dialog Triggers
    var reviewToEdit by remember { mutableStateOf<ReviewModel?>(null) }
    var reviewToDelete by remember { mutableStateOf<ReviewModel?>(null) }
    var reviewToReport by remember { mutableStateOf<ReviewModel?>(null) }
    var reviewToReply by remember { mutableStateOf<ReviewModel?>(null) }

    var replyInputText by remember { mutableStateOf("") }

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
                        if (navController.previousBackStackEntry != null) navController.popBackStack()
                        else activity?.finish()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = hubGreen, titleContentColor = Color.White)
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
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding).background(Color(0xFFF8F9FA))) {
            item {
                selectedBook?.let { book ->
                    Column(
                        modifier = Modifier.fillMaxWidth().background(hubGreen.copy(alpha = 0.05f)).padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Card(
                            elevation = CardDefaults.cardElevation(8.dp),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.width(160.dp).height(240.dp)
                        ) {
                            AsyncImage(model = book.imageUrl, contentDescription = null, contentScale = ContentScale.Crop)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(book.bookName, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Text(book.author, color = hubGreen)
                    }
                }
            }

            item {
                TabRow(selectedTabIndex = if (selectedTab == "Summary") 0 else 1) {
                    Tab(selected = selectedTab == "Summary", onClick = { selectedTab = "Summary" }, text = { Text("Summary") })
                    Tab(selected = selectedTab == "Reviews", onClick = { selectedTab = "Reviews" }, text = { Text("Reviews (${reviewList.size})") })
                }
            }

            if (selectedTab == "Summary") {
                item { Text(selectedBook?.summary ?: "No summary.", modifier = Modifier.padding(20.dp)) }
            } else {
                items(reviewList) { review ->
                    ReviewItemUi(
                        review = review,
                        currentUserId = currentUserId,
                        reviewViewModel = reviewViewModel,
                        onEditClick = { reviewToEdit = it },
                        onDeleteClick = { reviewToDelete = it },
                        onReportClick = { reviewToReport = it },
                        onReplyClick = { reviewToReply = it }
                    )
                }
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }

    // --- DIALOGS ---

    // 1. ADD / EDIT DIALOG
    if (showReviewDialog || reviewToEdit != null) {
        val editing = reviewToEdit != null
        val review = reviewToEdit

        // Use remember for temporary edits within the dialog
        var title by remember { mutableStateOf(review?.title ?: "") }
        var content by remember { mutableStateOf(review?.content ?: "") }
        var rating by remember { mutableStateOf(review?.rating?.toInt() ?: 5) }

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
                        label = { Text("Comment") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // --- STAR RATING ROW ---
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Rating: ", fontWeight = FontWeight.Bold)
                        for (i in 1..5) {
                            IconButton(onClick = { rating = i }) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = if (i <= rating) Color(0xFFFFD700) else Color.Gray
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val finalReview = (review ?: ReviewModel()).copy(
                            bookId = bookId,
                            userId = currentUserId,
                            title = title,
                            content = content,
                            rating = rating.toDouble(), // Properly saving the selected stars
                            date = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date())
                        )
                        reviewViewModel.saveOrUpdateReview(finalReview, editing, bookId) {
                            showReviewDialog = false
                            reviewToEdit = null
                        }
                    },
                    enabled = title.isNotBlank() && content.isNotBlank()
                ) {
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
    // 2. REPLY DIALOG
    if (reviewToReply != null) {
        AlertDialog(
            onDismissRequest = { reviewToReply = null },
            title = { Text("Reply to Review") },
            text = {
                OutlinedTextField(
                    value = replyInputText,
                    onValueChange = { replyInputText = it },
                    label = { Text("Your response") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(onClick = {
                    reviewToReply?.let {
                        reviewViewModel.addReply(it, replyInputText, currentUserId) { success ->
                            if (success) {
                                Toast.makeText(context, "Reply sent!", Toast.LENGTH_SHORT).show()
                                replyInputText = ""
                                reviewToReply = null
                            }
                        }
                    }
                }, enabled = replyInputText.isNotBlank()) { Text("Send") }
            },
            dismissButton = { TextButton(onClick = { reviewToReply = null }) { Text("Cancel") } }
        )
    }

    // 3. DELETE DIALOG
    if (reviewToDelete != null) {
        AlertDialog(
            onDismissRequest = { reviewToDelete = null },
            title = { Text("Delete Review") },
            text = { Text("Confirm deletion?") },
            confirmButton = {
                Button(onClick = {
                    reviewToDelete?.let { reviewViewModel.deleteReview(it.reviewId, bookId) }
                    reviewToDelete = null
                }) { Text("Delete") }
            }
        )
    }

    // 4. REPORT DIALOG
    if (reviewToReport != null) {
        AlertDialog(
            onDismissRequest = { reviewToReport = null },
            title = { Text("Report Review") },
            confirmButton = {
                Button(onClick = {
                    reviewToReport?.let { reviewViewModel.reportReview(it.reviewId) }
                    reviewToReport = null
                    Toast.makeText(context, "Reported", Toast.LENGTH_SHORT).show()
                }) { Text("Report") }
            }
        )
    }
}

@Composable
fun ReviewItemUi(
    review: ReviewModel,
    currentUserId: String,
    reviewViewModel: ReviewViewModel,
    onEditClick: (ReviewModel) -> Unit,
    onDeleteClick: (ReviewModel) -> Unit,
    onReportClick: (ReviewModel) -> Unit,
    onReplyClick: (ReviewModel) -> Unit
) {
    val isOwnReview = review.userId == currentUserId
    val isLiked = review.likedBy.containsKey(currentUserId)
    var replies by remember { mutableStateOf<List<ReplyModel>>(emptyList()) }
    var showReplies by remember { mutableStateOf(false) }

    LaunchedEffect(review.reviewId) {
        reviewViewModel.fetchReplies(review.reviewId) { fetched -> replies = fetched }
    }

    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(review.title, fontWeight = FontWeight.Bold)
            Text(review.content, fontSize = 14.sp, color = Color.Gray)

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (isOwnReview) {
                    TextButton(onClick = { onEditClick(review) }) { Text("Edit") }
                    TextButton(onClick = { onDeleteClick(review) }) { Text("Delete", color = Color.Red) }
                } else {
                    TextButton(onClick = { reviewViewModel.toggleLike(review, currentUserId) }) {
                        Text(if (isLiked) "Unlike" else "Like", color = if (isLiked) Color.Red else Color.Gray)
                    }
                    TextButton(onClick = { onReplyClick(review) }) { Text("Reply") }
                    IconButton(onClick = { onReportClick(review) }) {
                        Icon(painterResource(R.drawable.outline_flag_2_24), null, tint = Color.Gray)
                    }
                }
            }

            if (replies.isNotEmpty()) {
                TextButton(onClick = { showReplies = !showReplies }) {
                    Text(if (showReplies) "Hide Replies" else "View Replies (${replies.size})", fontSize = 12.sp)
                }
                if (showReplies) {
                    replies.forEach { reply ->
                        Surface(
                            color = Color.Gray.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.padding(start = 12.dp, top = 4.dp).fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(6.dp)) {
                                Text(reply.content, fontSize = 13.sp)
                                Text("By ${reply.userId.take(5)}... on ${reply.date}", fontSize = 10.sp, color = Color.Gray)
                            }
                        }
                    }
                }
            }
        }
    }
}