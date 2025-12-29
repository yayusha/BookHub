package com.example.sem3project.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sem3project.R
import com.example.sem3project.ui.theme.White20

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
fun BookDetailsScreen() {
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
                IconButton(onClick = {}) {
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
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_star_rate_24),
                            contentDescription = "Rating Star",
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "4.8",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextColor,
                            modifier = Modifier.padding(start = 4.dp, end = 4.dp)
                        )
                        Text(
                            text = "(230)",
                            fontSize = 16.sp,
                            color = TextColor
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    val description =
                        "A dragon without its rider is a tragedy. A rider without their dragon is dead. " +
                                "The ancient bond between human and beast is broken, and a new threat looms. " +
                                "For the kingdom of Veridia to survive, a new pact must be forged, but the cost may be too high. " +
                                "God Of Ruin is the first book in the Legacy of Gods series."
                    val maxLines = 3

                    Text(
                        text = description,
                        fontSize = 14.sp,
                        color = GrayTextColor,
                        maxLines = if (isDescriptionExpanded) Int.MAX_VALUE else maxLines,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (!isDescriptionExpanded && description.length > 100) {
                        Text(
                            text = "Read More",
                            color = Color.Blue,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .clickable { isDescriptionExpanded = true }
                        )
                    } else if (isDescriptionExpanded) {
                        Text(
                            text = "Show Less",
                            color = Color.Blue,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .clickable { isDescriptionExpanded = false }
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            isAddedToList = !isAddedToList
                        },
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, buttonBorderColor),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text(
                            text = buttonText,
                            color = buttonColor,
                            fontWeight = FontWeight.SemiBold
                        )
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
                }
            }
        }
    }
}

