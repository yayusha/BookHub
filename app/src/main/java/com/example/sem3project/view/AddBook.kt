package com.example.sem3project.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import com.example.sem3project.R
import com.example.sem3project.model.BookModel
import com.example.sem3project.repo.BookRepoImpl
import com.example.sem3project.view.ui.theme.green20
import com.example.sem3project.viewmodel.BookViewModel

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

    val bookViewModel = remember { BookViewModel(BookRepoImpl()) }

    var bookName by remember { mutableStateOf("") }
    var bookAuthor by remember { mutableStateOf("") }
    var bookDescription by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            Text("Book", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = green20)
            Text("Hub", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = green20)
        }

        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.placeholder),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            FloatingActionButton(
                onClick = { },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 8.dp, y = 8.dp)
                    .size(44.dp),
                containerColor = green20
            ) {
                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("BOOK NAME", modifier = Modifier.fillMaxWidth())
        OutlinedTextField(
            value = bookName,
            onValueChange = { bookName = it },
            placeholder = { Text("Book Name") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            shape = RoundedCornerShape(12.dp)
        )

        Text("AUTHOR", modifier = Modifier.fillMaxWidth())
        OutlinedTextField(
            value = bookAuthor,
            onValueChange = { bookAuthor = it },
            placeholder = { Text("John Doe") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            shape = RoundedCornerShape(12.dp)
        )

        Text("DESCRIPTION", modifier = Modifier.fillMaxWidth())
        OutlinedTextField(
            value = bookDescription,
            onValueChange = { bookDescription = it },
            placeholder = { Text("Description") },
            modifier = Modifier.fillMaxWidth().height(120.dp).padding(vertical = 8.dp),
            shape = RoundedCornerShape(12.dp),
            maxLines = 5
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = {
                if (bookName.isBlank() || bookAuthor.isBlank() || bookDescription.isBlank()) {
                    Toast.makeText(context, "Please fill all required fields", Toast.LENGTH_LONG).show()
                } else {
                    val model = BookModel(
                        bookName = bookName,
                        author = bookAuthor,
                        description = bookDescription
                    )
                    bookViewModel.addBook(model)
                    { success, message ->
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = green20),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text("Add", fontSize = 18.sp, color = Color.White)
        }
    }



}