package com.example.sem3project.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.sem3project.R
import com.example.sem3project.repo.BookRepoImpl
import com.example.sem3project.viewmodel.BookViewModel
import com.example.sem3project.viewmodel.BookViewModelFactory

@Composable
fun HomeScreen(navController: NavController) {
    val repo = BookRepoImpl()
    val factory = BookViewModelFactory(repo)
    val bookViewModel: BookViewModel = viewModel(factory = factory)

    val books = bookViewModel.bookListState.value

    if (books.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No books available")
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(books) { book ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate("detail/${book.bookId}") }
            ) {
                Row(modifier = Modifier.padding(12.dp)) {
                    AsyncImage(
                        model = book.imageUrl,
                        contentDescription = book.bookName,
                        modifier = Modifier.size(60.dp).clip(MaterialTheme.shapes.small),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(book.bookName, style = MaterialTheme.typography.titleMedium)
                        Text(book.author, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
