package com.example.sem3project.view

import android.net.Uri
import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.sem3project.R
import com.example.sem3project.model.BookModel
import com.example.sem3project.repo.BookRepoImpl
import com.example.sem3project.repo.ImageRepoImpl
import com.example.sem3project.utils.ImageUtils
import com.example.sem3project.utils.NotificationHelper
import com.example.sem3project.view.ui.theme.green20
import com.example.sem3project.viewmodel.BookViewModel
import com.example.sem3project.viewmodel.ImageViewModel

class AddBook : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AddBookScreen()
        }
    }
}

    @Composable
    fun AddBookScreen() {
        val context = LocalContext.current
        val notificationPermissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            // Handle permission result if needed
        }
        LaunchedEffect(Unit) {
            NotificationHelper.createNotificationChannel(context)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }


        val bookViewModel = remember { BookViewModel(BookRepoImpl()) }
        val imageViewModel = remember { ImageViewModel(ImageRepoImpl()) }

        var bookName by remember { mutableStateOf("") }
        var bookGenre by remember { mutableStateOf("") }
        var bookAuthor by remember { mutableStateOf("") }
        var bookDescription by remember { mutableStateOf("") }
        var imageUri by remember { mutableStateOf<Uri?>(null) }
        var cloudinaryLink by remember { mutableStateOf("") }

        val imagePickerLauncher =
            rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
            ) { uri ->
                imageUri = uri
                imageUri?.let {
                    imageViewModel.uploadImage(context, it) { success, message ->
                        if (success) {
                            cloudinaryLink = message.toString()
                        }
                    }
                }
            }


        Scaffold { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Text(
                    text = "Add Book",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold

                )

                // Image Picker
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clickable { imagePickerLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (imageUri == null) {
                        Image(
                            painter = painterResource(R.drawable.placeholder),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painter = rememberAsyncImagePainter(imageUri),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 🔹 INPUT FIELDS
                OutlinedTextField(
                    value = bookName,
                    onValueChange = { bookName = it },
                    placeholder = { Text("Book Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = bookAuthor,
                    onValueChange = { bookAuthor = it },
                    placeholder = { Text("Author") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = bookGenre,
                    onValueChange = { bookGenre = it },
                    placeholder = { Text("Genre") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = bookDescription,
                    onValueChange = { bookDescription = it },
                    placeholder = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 4
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        val book = BookModel(
                            bookName = bookName,
                            author = bookAuthor,
                            genreId = bookGenre,
                            description = bookDescription,
                            imageUrl = cloudinaryLink
                        )
                        bookViewModel.addBook(context,book) { success, message ->
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = green20,   //bg
                        contentColor = Color.White
                    )
                )
                {
                    Text("Add Book")
                }


            }
        }

    }

