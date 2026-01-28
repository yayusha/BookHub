package com.example.sem3project.view

import android.app.Activity
import android.content.Intent
import java.text.SimpleDateFormat
import java.util.Locale
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sem3project.R
import com.example.sem3project.model.ReviewModel
import com.example.sem3project.ui.theme.White20
import com.example.sem3project.viewmodel.ReviewViewModel
import java.util.Date

class Details : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BookDetailsScreen()
        }
    }
}

@Composable
fun BookDetailsScreen(reviewViewModel: ReviewViewModel = viewModel<ReviewViewModel>()) {

    var isDescriptionExpanded by remember { mutableStateOf(false) }
    var isSummaryExpanded by remember { mutableStateOf(false) }
    var isAddedToList by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf("Summary") }

    val buttonText = if (isAddedToList) "Remove from List" else "Add to List"
    val buttonColor = if (isAddedToList) Color.Red else Color.Black
    val buttonBorderColor = if (isAddedToList) Color.Red else Color.Transparent.copy(alpha = 0.5f)

    val BookHubGreen = colorResource(R.color.Hub)
    val TextColor = colorResource(R.color.Text)
    val GrayTextColor = colorResource(R.color.Grey)

    val context = LocalContext.current

// data Observation
    val reviewList by reviewViewModel.reviews

//    rating
    val averageRating = if(reviewList.isNotEmpty()) reviewList.map { it.rating }.average() else 0.0
    val totalReviews= reviewList.size

//    for review dialog
    var showReviewDialog by remember {mutableStateOf(false)}
    var reviewTitle by remember {mutableStateOf("")}
    var reviewContent by remember {mutableStateOf("")}
    var reviewRating: Int by remember {mutableStateOf(5)}




    val listData= listOf(
        "Fantasy","Contemporary","Romance",
        "New adult",
        "Fiction"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(White20)
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BookHubGreen)
                    .padding(top = 32.dp, bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Navigation Icon
                IconButton(onClick = {(context as? Activity)?.finish()}) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_arrow_back_24),
                        contentDescription = null,
                        tint = White20
                    )
                }

                Text(
                    "Detail",
                    modifier = Modifier.weight(1f),
                    style = TextStyle(
                        textAlign = TextAlign.Center,
                        fontSize = 28.sp,
                        color = White20,
                        fontWeight = FontWeight.Light,
                        fontFamily = FontFamily.SansSerif
                    )
                )

                IconButton(onClick = {}) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_favorite_24),
                        contentDescription = null,
                        tint = White20
                    )
                }
             
            }
        }


//        BOOK INFO
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                // Book Cover Image
                Image(
                    painter = painterResource(R.drawable.godofruin),
                    contentDescription = "Book Cover",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(130.dp)
                        .height(190.dp)
                        .clip(RoundedCornerShape(8.dp))
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp, vertical = 0.dp)
                ) {
                    Text(
                        text = "God Of Ruin",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextColor
                    )
                    Text(
                        text = "Rina Kent",
                        fontSize = 16.sp,
                        color = TextColor,
//                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_star_rate_24),
                            contentDescription = "Rating Star",
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "%.1f".format(averageRating),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextColor,
                            modifier = Modifier.padding(start = 4.dp, end = 4.dp)
                        )
                        Text(
                            text = "($totalReviews)",
                            fontSize = 16.sp,
                            color = TextColor,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))


                    val description =
                        "A dragon without its rider is a tragedy. A rider without their dragon is dead. " +
                                "The ancient bond between human and beast is broken, and a new threat looms. " +
                                "For the kingdom of Veridia to survive, a new pact must be forged, but the cost may be too high. " +
                                "God Of Ruin is the first book in the Legacy of Gods series."

                    Text(
                        text = description,
                        fontSize = 14.sp,
                        color = GrayTextColor,
                        maxLines = if (isDescriptionExpanded) Int.MAX_VALUE else 3,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = if (isDescriptionExpanded) "Show Less" else "Read More",
                        color = Color.Blue,
                        fontSize = 14.sp,
                        modifier = Modifier.clickable { isDescriptionExpanded = !isDescriptionExpanded }.padding(top = 2.dp)
                    )


//                    ADD TO LIST
                    OutlinedButton(
                        onClick = {
                            reviewViewModel.toggleBookInList("user_123","book_456", isAddedToList){
                                sucess ->
                                if( sucess) isAddedToList = !isAddedToList
                            }
                        },
                        shape= RoundedCornerShape(8.dp),
                        border= BorderStroke(1.dp, buttonBorderColor),
                        modifier = Modifier.padding(top =16.dp)
                    ) {
                        Text(text= buttonText,
                            color =buttonColor,
                            fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

//SUMMARY
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Summary Button
                Button(
                    onClick = { selectedTab = "Summary" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTab == "Summary") BookHubGreen else GrayTextColor.copy(
                            alpha = 0.1f
                        )
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f).height(48.dp)
                ) {
                    Text(
                        "Summary",
                        color = if (selectedTab == "Summary") White20 else GrayTextColor,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Review Button
                Button(
                    onClick = { selectedTab = "Reviews" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTab == "Reviews") BookHubGreen else GrayTextColor.copy(
                            alpha = 0.1f
                        )
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f).height(48.dp)
                ) {
                    Text(
                        "Reviews",
                        color = if (selectedTab == "Reviews") White20 else GrayTextColor,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(9.dp))
        }

        // GENRE TAGS
        item {
            LazyRow(
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                items(listData.size) { index ->
                    Text(
                        listData[index],
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color= Color.DarkGray,
                        modifier = Modifier.padding(end = 6.dp, start = 16.dp)
                    )
                }
            }
        }


        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                if (selectedTab == "Summary") {
                    val summaryText =
                        "This is the summary part. here we can see the summary of the book. From USA Today bestselling author Rina Kent comes the fourth in a dark and steamy college romance series featuring elite criminal families and morally gray heroes. " +
                                "Mia Solokov has one goal: ruin Landon King for messing with her family. But this sociopathic, genius artist might just ruin her instead. Landon is a complex character whose love for Mia drives him to change and grow.Landon asks her father for permission to date his daughter, which Jamie's father reluctantly allows after Landon professes his feelings for Jamie."
                    val maxLinesForSummary = 4
                    Text(
                        text = summaryText,
                        fontSize = 16.sp,
                        color = TextColor,
                        maxLines = if (isSummaryExpanded) Int.MAX_VALUE else maxLinesForSummary,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                    if (!isSummaryExpanded && summaryText.length > 90) {
                        Text(
                            text = "Read More",
                            color = Color.Blue,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .clickable { isSummaryExpanded = true }
                        )
                    } else if (isSummaryExpanded) {
                        Text(
                            text = "Show Less",
                            color = Color.Blue,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .clickable { isSummaryExpanded = false }
                        )
                    }
                } else {
                    if (reviewList.isEmpty()) {
                        Text(
                            "No Reviews yet.",
                            color = Color.Gray,
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    } else {
                        reviewList.forEach { review ->
                            ReviewItemUi(review)
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }

                    Button(
                        onClick = {
                            showReviewDialog =true},
                        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BookHubGreen),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Write a Review", color = Color.White)
                    }
                }
            }
        }
    }
    // Review dialog
    if (showReviewDialog) {
        AlertDialog(
            onDismissRequest = { showReviewDialog = false },
            title = { Text("Write a Review") },
            text = {
                Column {
                    OutlinedTextField(value = reviewTitle, onValueChange = { reviewTitle = it }, label = { Text("Title") }, modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(value = reviewContent, onValueChange = { reviewContent = it }, label = { Text("Content") }, modifier = Modifier.fillMaxWidth(), minLines = 3)
                    Spacer(Modifier.height(8.dp))
                    Text("Rating: $reviewRating Stars")
                    Slider(value = reviewRating.toFloat(), onValueChange = { reviewRating = it.toInt() }, valueRange = 1f..5f, steps = 3)
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (reviewTitle.isNotBlank() && reviewContent.isNotBlank()) {
                    val newReview = ReviewModel(
                        id = "",
                        title = reviewTitle,
                        content = reviewContent,
                        rating = reviewRating.toDouble(),
                        date = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date()
                        )
                    )

                        reviewViewModel.addReview(newReview) { success ->
                            if (success) {
                                showReviewDialog = false
                                Toast.makeText(context, "Review submitted!", Toast.LENGTH_SHORT).show()

                                reviewTitle = ""
                                reviewContent = ""
                                reviewRating = 5
                            } else {
                                Toast.makeText(context, "Failed to submit review", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                    }
                },
                    colors = ButtonDefaults.buttonColors(containerColor = BookHubGreen)
                ) {
                    Text("Submit", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = {showReviewDialog = false}) {
                    Text("Cancel", color=Color.Gray)
                }
            }
        )

    }
}


@Composable
fun ReviewItemUi(review: ReviewModel) {
    Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(Color.White).padding(18.dp)) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.LightGray))
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(review.date, fontSize = 12.sp, color = Color.Gray)
                    Text(review.title, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(review.content, fontSize = 14.sp, color = Color.DarkGray)
            Row(modifier = Modifier.padding(top = 4.dp)) {
                repeat(review.rating.toInt()) {
                    Icon(painter = painterResource(R.drawable.baseline_star_rate_24), contentDescription = null, tint = Color(0xFFFFC107), modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

