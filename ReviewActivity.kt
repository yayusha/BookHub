package com.example.projectsem3
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectsem3.ui.theme.ProjectSem3Theme

class ReviewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ProjectSem3Theme {
                Scaffold { innerPadding ->
                    BookDetailScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun BookDetailScreen(modifier: Modifier = Modifier) {

    val BookHubGreen = Color(0xFF4CAF50)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9FB))
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        // ---------- BOOKHUB LOGO ----------
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Book", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text("Hub", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = BookHubGreen)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ---------- TOP BAR ----------
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                painter = painterResource(id = R.drawable.outline_arrow_back_ios_24),
                contentDescription = null,
                modifier = Modifier.size(25.dp),
                tint = Color.Black
            )

            Text(
                text = "Detail",
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
            )

            Icon(
                painter = painterResource(id = R.drawable.outline_favorite_24),
                contentDescription = null,
                modifier = Modifier.size(25.dp),
                tint = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ---------- BOOK COVER + DETAILS ----------
        Row {
            Image(
                painter = painterResource(id = R.drawable.god_of_ruin),
                contentDescription = null,
                modifier = Modifier
                    .size(135.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.width(18.dp))

            Column {

                Text("God Of Ruin", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("Rina Kent", fontSize = 14.sp, color = Color.Gray)

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Icon(
                        painter = painterResource(id = R.drawable.outline_kid_star_24),
                        contentDescription = null,
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(18.dp)
                    )

                    Text(" 4.8 ", fontWeight = FontWeight.Bold)
                    Text("(230)", color = Color.Gray, fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "A dragon without its rider is a tragedy. A rider without their dragon is dead... Read More",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        // ---------- TAB CONTAINER (ONE BOX) ----------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFF2F2F2))
                .padding(8.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                // Summary tab
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFE5E5E5))
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Summary", fontSize = 15.sp)
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Reviews tab (selected)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(BookHubGreen)
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Reviews", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(25.dp))

        // ---------- REVIEW CARD ----------
        ReviewCardUI()

        Spacer(modifier = Modifier.height(40.dp))

        // ---------- BUTTON ----------
        Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BookHubGreen)
        ) {
            Text(
                text = "Write A Review",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun ReviewCardUI() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(18.dp)
    ) {

        Column {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text("20/12/2020", fontSize = 12.sp, color = Color.Gray)
                        Text("Great Book", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Icon(
                    painter = painterResource(id = R.drawable.outline_more_horiz_24),
                    contentDescription = null,
                    tint = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                repeat(5) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_kid_star_24),
                        contentDescription = null,
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Honestly, I can't get over them. The fact that I wanted to cry after reading Landon’s letter to Mia and learning they’re having a baby made me upgrade my rating from 4.5 to 5 stars.",
                color = Color.DarkGray,
                fontSize = 14.sp
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewScreen() {
    ProjectSem3Theme { BookDetailScreen() }
}
