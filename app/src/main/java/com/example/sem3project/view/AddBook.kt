package com.example.sem3project.view

import android.net.Uri
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
import androidx.compose.material.icons.filled.ArrowBack
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
import coil.compose.rememberAsyncImagePainter
import com.example.sem3project.R
import com.example.sem3project.model.BookModel
import com.example.sem3project.repo.BookRepoImpl
import com.example.sem3project.repo.ImageRepoImpl
import com.example.sem3project.ui.theme.green20
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookScreen() {
    val context = LocalContext.current
    val activity = context as? android.app.Activity

    // ✅ Manually initializing ViewModels with Repos to prevent constructor crashes
    val bookViewModel = remember { BookViewModel(BookRepoImpl()) }
    val imageViewModel = remember { ImageViewModel(ImageRepoImpl()) }

    var bookName by remember { mutableStateOf("") }
    var bookGenre by remember { mutableStateOf("") }
    var bookAuthor by remember { mutableStateOf("") }
    var bookDescription by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var cloudinaryLink by remember { mutableStateOf("") }
    var isUploading by remember { mutableStateOf(false) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            imageUri = it
            isUploading = true
            imageViewModel.uploadImage(context, it) { success, message ->
                isUploading = false
                if (success) {
                    cloudinaryLink = message.toString()
                } else {
                    Toast.makeText(context, "Upload failed: $message", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Book", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { activity?.finish() }) {
                        // ✅ Changed to standard Icon to prevent drawable-missing crash
                        Icon(Icons.Default.ArrowBack, "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = green20)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Gray)
                    .clickable { imagePickerLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (imageUri == null) {
                    Image(painterResource(R.drawable.placeholder), null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                } else {
                    Image(rememberAsyncImagePainter(imageUri), null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                }
                if (isUploading) CircularProgressIndicator(color = Color.White)
            }

            OutlinedTextField(value = bookName, onValueChange = { bookName = it }, label = { Text("Book Name") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = bookAuthor, onValueChange = { bookAuthor = it }, label = { Text("Author") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = bookGenre, onValueChange = { bookGenre = it }, label = { Text("Genre") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = bookDescription, onValueChange = { bookDescription = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth(), maxLines = 4)

            Button(
                onClick = {
                    if (cloudinaryLink.isEmpty()) {
                        Toast.makeText(context, "Please upload an image first", Toast.LENGTH_SHORT).show()
                    } else if (bookName.isBlank()) {
                        Toast.makeText(context, "Please enter a book name", Toast.LENGTH_SHORT).show()
                    } else {
                        val book = BookModel(
                            bookName = bookName,
                            author = bookAuthor,
                            genreId = bookGenre,
                            description = bookDescription,
                            imageUrl = cloudinaryLink
                        )
                        bookViewModel.addBook(context, book) { success, message ->
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            if (success) activity?.finish()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = green20)
            ) {
                Text("Add Book", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}