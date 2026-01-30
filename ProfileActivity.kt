package com.example.projectsem3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectsem3.ui.theme.ProjectSem3Theme

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjectSem3Theme {
                ProfileBody()
            }
        }
    }
}

@Composable
fun ProfileBody() {

    var selectedTab by remember { mutableStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        // -------------------- TOP BAR --------------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                painter = painterResource(R.drawable.outline_arrow_back_ios_new_24),
                contentDescription="Back",
                tint = Color.Black,
                modifier = Modifier
                    .size(26.dp)
                    .clickable{}
            )

            Text(
                text = "Profile",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Icon(
                painter = painterResource(R.drawable.outline_edit_24),
                contentDescription = "Edit",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(22.dp)
                    .clickable{}
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // -------------------- PROFILE ROW --------------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Avatar
            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = "Profile",
                modifier = Modifier
                    .size(125.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(20.dp))

            // Followers | Following on right of avatar
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    StatMini("120", "Followers")
                    Spacer(modifier = Modifier.width(36.dp))
                    StatMini("80", "Following")
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // -------------------- NAME + BIO CENTERED --------------------
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text("Alex John", fontSize = 30.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                "I read books.",
                fontSize = 20.sp,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // -------------------- BOOKS REVIEWED + READ --------------------
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatLarge("5", "Books Reviewed")
            StatLarge("15", "Books Read")
        }

        Spacer(modifier = Modifier.height(25.dp))

        Divider(color = Color(0xFFEAEAEA), thickness = 1.dp)

        Spacer(modifier = Modifier.height(20.dp))

        // -------------------- TABS --------------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            TabButton(
                text = "Wish List",
                selected = selectedTab == 0,
                modifier = Modifier.weight(1f)
            ) { selectedTab = 0 }

            Spacer(modifier = Modifier.width(10.dp))

            TabButton(
                text = "My Reviews",
                selected = selectedTab == 1,
                modifier = Modifier.weight(1f)
            ) { selectedTab = 1 }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (selectedTab == 1) {
            ReviewList()
        } else {
            WishListPlaceholder()
        }
    }
}

// ---------------- SMALL FOLLOWERS | FOLLOWING STYLE ----------------
@Composable
fun StatMini(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontSize = 17.sp, fontWeight = FontWeight.Bold)
        Text(label, fontSize = 12.sp, color = Color.Gray)
    }
}

// ---------------- LARGE BOOK STATS ----------------
@Composable
fun StatLarge(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Text(label, fontSize = 12.sp, color = Color.Gray)
    }
}

// ---------------- TAB BUTTON ----------------
@Composable
fun TabButton(text: String, selected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .height(44.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (selected) Color(0xFF00A36C) else Color(0xFFF0F0F0))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (selected) Color.White else Color.Black,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

// ---------------- REVIEW LIST ----------------
@Composable
fun ReviewList() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {

        repeat(2) {
            ReviewCard()
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// ---------------- REVIEW CARD ----------------
@Composable
fun ReviewCard() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(18.dp))
            .background(Color.White, RoundedCornerShape(18.dp))
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row {
                Image(
                    painter = painterResource(id = R.drawable.godofruin),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {

                    Text("20/12/2020", fontSize = 12.sp, color = Color.Gray)

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        "Great Book",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Row {
                        repeat(5) {
                            Text("⭐", fontSize = 13.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        "Honestly, I can’t get over them. The fact that I wanted to cry after reading Landon’s letter to Mia and learning they're having a baby made me want to upgrade my rating from 4.5 to 5 stars",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        maxLines = 3
                    )
                }
            }

            Icon(
                Icons.Default.MoreVert,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}

@Composable
fun WishListPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Your wishlist is empty.", color = Color.Gray, fontSize = 14.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProfile() {
    ProjectSem3Theme {
        ProfileBody()
    }
}
