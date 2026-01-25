package com.example.sem3project.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sem3project.R

@Composable
fun ProfileScreen() {

    var selectedTab by remember { mutableStateOf(1) }
    var menuExpanded by remember { mutableStateOf(false) }
    var showDeactivateDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentPadding = PaddingValues(bottom = 20.dp)
    ) {

        /* ---------------- TOP BAR ---------------- */
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Icon(
                    painter = painterResource(R.drawable.baseline_arrow_back_24),
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(26.dp)
                        .clickable { }
                )

                Text(
                    text = "Profile",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Box {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Menu",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { menuExpanded = true }
                    )

                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Deactivate Account") },
                            onClick = {
                                menuExpanded = false
                                showDeactivateDialog = true
                            }
                        )
                    }
                }
            }
        }

        /* ---------------- PROFILE IMAGE + FOLLOW BUTTONS ---------------- */
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(id = R.drawable.icon),
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(125.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(20.dp))

                Row {
                    StatMiniButton("120", "Followers") { }

                    Spacer(modifier = Modifier.width(20.dp))

                    StatMiniButton("80", "Following") { }
                }
            }
        }

        /* ---------------- NAME & BIO ---------------- */
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Alex John",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "I read books.",
                    fontSize = 15.sp,
                    color = Color.Gray
                )
            }
        }

        /* ---------------- STATS ---------------- */
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatLarge("5", "Books Reviewed")
                StatLarge("15", "Books Read")
            }
        }

        item {
            Divider(color = Color(0xFFEAEAEA), thickness = 1.dp)
        }

        /* ---------------- TABS ---------------- */
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
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
        }

        /* ---------------- CONTENT ---------------- */
        if (selectedTab == 1) {

            val reviews = listOf(1, 2, 3, 4, 5)

            items(reviews) {
                ReviewCard()
                Spacer(modifier = Modifier.height(16.dp))
            }

        } else {
            item {
                WishListPlaceholder()
            }
        }
    }

    /* ---------------- DEACTIVATE DIALOG ---------------- */
    if (showDeactivateDialog) {
        AlertDialog(
            onDismissRequest = { showDeactivateDialog = false },
            title = { Text("Deactivate Account") },
            text = {
                Text("Are you sure you want to deactivate your account? You can reactivate anytime by logging in again.")
            },
            confirmButton = {
                TextButton(onClick = {
                    showDeactivateDialog = false
                    // TODO: Add deactivate logic here
                }) {
                    Text("Deactivate")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeactivateDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

/* ---------------- COMPONENTS ---------------- */

@Composable
fun StatMiniButton(value: String, label: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value, fontSize = 17.sp, fontWeight = FontWeight.Bold)
        Text(label, fontSize = 12.sp, color = Color.Gray)
    }
}

@Composable
fun StatLarge(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Text(label, fontSize = 12.sp, color = Color.Gray)
    }
}

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

@Composable
fun ReviewCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
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
                        text = "Great Book",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Row {
                        repeat(5) { Text("⭐", fontSize = 13.sp) }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Honestly, I can’t get over them. The fact that I wanted to cry after reading Landon’s letter...",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        maxLines = 3
                    )
                }
            }

            Icon(Icons.Default.MoreVert, contentDescription = null, tint = Color.Gray)
        }
    }
}

@Composable
fun WishListPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        Text("Your wishlist is empty.", color = Color.Gray, fontSize = 14.sp)
    }
}
