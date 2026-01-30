package com.example.sem3project.view

import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.sem3project.R
import com.example.sem3project.model.ReviewModel
import com.example.sem3project.model.WishlistBook
import com.example.sem3project.repo.ProfileRepoImpl
import com.example.sem3project.viewmodel.ImageViewModel
import com.example.sem3project.viewmodel.ProfileViewModel
import com.example.sem3project.viewmodel.ProfileViewModelFactory
import com.example.sem3project.repo.ImageRepoImpl
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileScreen(
    navController: NavHostController, // Added to match navigation
    isDarkMode: Boolean,              // New parameter for theme state
    onThemeChange: (Boolean) -> Unit  // New parameter for toggle logic
) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val currentUserId = auth.currentUser?.uid ?: ""

    val profileViewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModelFactory(ProfileRepoImpl())
    )
    val imageViewModel = remember { ImageViewModel(ImageRepoImpl()) }

    val wishlist = profileViewModel.wishlist.value
    val reviewCount = profileViewModel.reviewCount.value
    val readCount = profileViewModel.readCount.value
    val myReviews = profileViewModel.myReviews.value
    val profileData by profileViewModel.profile.observeAsState()

    var selectedTab by remember { mutableStateOf(0) }
    var menuExpanded by remember { mutableStateOf(false) }
    var showDeactivateDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var deletePassword by remember { mutableStateOf("") }
    var isUploading by remember { mutableStateOf(false) }

    LaunchedEffect(currentUserId) {
        if (currentUserId.isNotEmpty()) {
            profileViewModel.loadUserData(currentUserId)
            profileViewModel.getProfileById(currentUserId)
        }
    }

    fun navigateToActivity(activityClass: Class<*>) {
        val intent = Intent(context, activityClass)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            isUploading = true
            imageViewModel.uploadImage(context, it) { success, _ ->
                isUploading = false
                if (success) Toast.makeText(context, "Image Updated!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background), // Uses theme background
        contentPadding = PaddingValues(bottom = 20.dp)
    ) {
        // ---------------- TOP BAR ----------------
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
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .size(26.dp)
                        .clickable { navController.popBackStack() }
                )
                Text(
                    "Profile",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Box {
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(Icons.Default.MoreVert, "Menu", tint = MaterialTheme.colorScheme.onBackground)
                    }
                    DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                        DropdownMenuItem(
                            text = { Text("Sign Out") },
                            onClick = {
                                menuExpanded = false
                                auth.signOut()
                                navigateToActivity(com.example.sem3project.view.LoginActivity::class.java)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Deactivate Account") },
                            onClick = { menuExpanded = false; showDeactivateDialog = true }
                        )
                        DropdownMenuItem(
                            text = { Text("Delete Account", color = Color.Red) },
                            onClick = { menuExpanded = false; showDeleteDialog = true }
                        )
                    }
                }
            }
        }

        // ---------------- PROFILE IMAGE ----------------
        item {
            Box(Modifier.fillMaxWidth().padding(vertical = 10.dp), Alignment.Center) {
                Box(Modifier.size(125.dp).clip(CircleShape).clickable { imagePickerLauncher.launch("image/*") }) {
                    AsyncImage(
                        model = if (profileData?.imageUrl?.isNotEmpty() == true) profileData?.imageUrl else R.drawable.icon,
                        contentDescription = "Profile",
                        modifier = Modifier.fillMaxSize().clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    if (isUploading) CircularProgressIndicator(Modifier.align(Alignment.Center), color = Color.Green)
                }
            }
        }

        // ---------------- NAME & EMAIL ----------------
        item {
            Column(
                modifier = Modifier.fillMaxWidth().padding(top = 14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val fullName = "${profileData?.firstName ?: ""} ${profileData?.lastName ?: ""}".trim()
                Text(
                    fullName.ifEmpty { "User Name" },
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    profileData?.email ?: "user@email.com",
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // ---------------- THEME SWITCH ----------------
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = if (isDarkMode) R.drawable.baseline_dark_mode_24 else R.drawable.baseline_light_mode_24),
                            contentDescription = if (isDarkMode) "Dark Mode" else "Light Mode",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.width(12.dp))
                        Text("Dark Mode", fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                    }
                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = { onThemeChange(it) }
                    )
                }
            }
        }

        // ---------------- STATS ----------------
        item {
            Row(Modifier.fillMaxWidth().padding(vertical = 24.dp), Arrangement.SpaceEvenly) {
                StatLarge(reviewCount.toString(), "My Reviews")
                StatLarge(wishlist.size.toString(), "Wishlist")
                StatLarge(readCount.toString(), "Books Read")
            }
            Divider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp)
        }

        // ---------------- TABS ----------------
        item {
            Row(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 10.dp)) {
                TabButton("Wish List", selectedTab == 0, Modifier.weight(1f)) { selectedTab = 0 }
                Spacer(Modifier.width(10.dp))
                TabButton("My Reviews", selectedTab == 1, Modifier.weight(1f)) { selectedTab = 1 }
            }
        }

        // ---------------- LIST CONTENT ----------------
        if (selectedTab == 0) {
            if (wishlist.isEmpty()) {
                item { WishListPlaceholder("Your wishlist is empty.") }
            } else {
                items(wishlist) { book ->
                    WishListItemRow(book) { profileViewModel.removeFromWishlist(currentUserId, book.id) }
                    Spacer(Modifier.height(12.dp))
                }
            }
        } else {
            if (myReviews.isEmpty()) {
                item { WishListPlaceholder("No reviews written yet.") }
            } else {
                items(myReviews) { review ->
                    ReviewCard(review)
                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }

    // --- DEACTIVATE DIALOG ---
    if (showDeactivateDialog) {
        AlertDialog(
            onDismissRequest = { showDeactivateDialog = false },
            title = { Text("Deactivate Account") },
            text = { Text("You will be signed out. You can log back in anytime to reactivate your profile.") },
            confirmButton = {
                TextButton(onClick = {
                    showDeactivateDialog = false
                    auth.signOut()
                    val intent = Intent(context, com.example.sem3project.view.RegistrationActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    context.startActivity(intent)
                }) { Text("Deactivate") }
            },
            dismissButton = { TextButton(onClick = { showDeactivateDialog = false }) { Text("Cancel") } }
        )
    }

    // --- DELETE DIALOG ---
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Permanently", color = Color.Red) },
            text = {
                Column {
                    Text("Warning: This will wipe all your reviews and wishlist forever. Enter password to confirm.")
                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(
                        value = deletePassword,
                        onValueChange = { deletePassword = it },
                        label = { Text("Confirm Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val user = auth.currentUser
                    if (user != null && deletePassword.isNotEmpty()) {
                        val credential = EmailAuthProvider.getCredential(user.email!!, deletePassword)
                        user.reauthenticate(credential).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                profileViewModel.deleteProfile(currentUserId) { dbSuccess, _ ->
                                    if (dbSuccess) {
                                        user.delete().addOnCompleteListener { authTask ->
                                            if (authTask.isSuccessful) {
                                                val intent = Intent(context, com.example.sem3project.view.RegistrationActivity::class.java)
                                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                context.startActivity(intent)
                                            }
                                        }
                                    }
                                }
                            } else {
                                Toast.makeText(context, "Wrong Password", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }) { Text("Delete Forever", color = Color.Red) }
            },
            dismissButton = { TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel") } }
        )
    }
}

@Composable
fun StatLarge(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
        Text(label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun TabButton(text: String, selected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .height(44.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (selected) Color(0xFF00A36C) else MaterialTheme.colorScheme.surfaceVariant)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun WishListItemRow(book: WishlistBook, onRemove: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(model = book.coverImageUrl, contentDescription = null, modifier = Modifier.size(60.dp, 90.dp).clip(RoundedCornerShape(8.dp)), contentScale = ContentScale.Crop)
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(book.title, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Text("In Wishlist", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            IconButton(onClick = onRemove) { Icon(painterResource(R.drawable.outline_delete_24), "Remove", tint = Color.Red) }
        }
    }
}

@Composable
fun ReviewCard(review: ReviewModel) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(review.title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
            Row { repeat(review.rating.toInt()) { Text("⭐") } }
            Spacer(Modifier.height(4.dp))
            Text(review.content, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun WishListPlaceholder(text: String) {
    Box(Modifier.fillMaxWidth().height(150.dp), Alignment.Center) {
        Text(text, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
    }
}