package com.example.sem3project.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sem3project.R
import com.example.sem3project.ui.theme.Sem3ProjectTheme
import com.google.firebase.database.*

// Data class for Review
data class Review(
    val date: String = "",
    val title: String = "",
    val content: String = "",
    val rating: Int = 5
)

class ReviewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Sem3ProjectTheme {
                Scaffold { innerPadding ->
                    BookDetailScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun BookDetailScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val BookHubGreen = Color(0xFF4CAF50)

    // Database State
    var reviewList by remember { mutableStateOf(listOf<Review>()) }
    val database = FirebaseDatabase.getInstance().getReference("reviews")

    // Fetch data from Firebase
    LaunchedEffect(Unit) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = mutableListOf<Review>()
                for (data in snapshot.children) {
                    val review = data.getValue(Review::class.java)
                    if (review != null) items.add(review)
                }
                reviewList = items
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9FB))
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        // --- LOGO ---
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Book", fontSize = 26.sp, fontWeight = FontWeight.Bold)
            Text("Hub", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = BookHubGreen)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- TOP BAR ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                contentDescription = "Back",
                modifier = Modifier.clickable { (context as? Activity)?.finish() }
            )
            Text("Detail", style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold))
            Icon(
                painter = painterResource(id = R.drawable.baseline_favorite_24),
                contentDescription = "Favorite"
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- BOOK DETAILS ---
        Row {
            Image(
                painter = painterResource(id = R.drawable.godofruin),
                contentDescription = null,
                modifier = Modifier.size(135.dp).clip(RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.width(18.dp))
            Column {
                Text("God Of Ruin", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("Rina Kent", fontSize = 14.sp, color = Color.Gray)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(painter = painterResource(id = R.drawable.baseline_star_rate_24), contentDescription = null, tint = Color(0xFFFFC107))
                    Text(" 4.8 ", fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(25.dp))

        // --- DYNAMIC REVIEWS FROM DB ---
        Text("Reviews", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(10.dp))

        if (reviewList.isEmpty()) {
            Text("No reviews yet.", color = Color.Gray)
        } else {
            reviewList.forEach { review ->
                ReviewCardUI(review)
                Spacer(modifier = Modifier.height(10.dp))
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // --- WRITE REVIEW BUTTON ---
        Button(
            onClick = { context.startActivity(Intent(context, New::class.java)) },
            modifier = Modifier.fillMaxWidth().height(58.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BookHubGreen)
        ) {
            Text("Write A Review", fontSize = 18.sp, color = Color.White)
        }
    }
}

@Composable
fun ReviewCardUI(review: Review) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(18.dp)
    ) {
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
        }
    }
}