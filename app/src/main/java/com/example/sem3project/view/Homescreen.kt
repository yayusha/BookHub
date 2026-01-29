package com.example.sem3project.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState // Requires: implementation("androidx.compose.runtime:runtime-livedata")
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.sem3project.model.BookModel
import com.example.sem3project.repo.BookRepoImpl
import com.example.sem3project.viewmodel.BookViewModel
import com.example.sem3project.viewmodel.BookViewModelFactory
@Composable
fun HomeScreen(navController: NavController) {
    val repo = BookRepoImpl()
    val factory = BookViewModelFactory(repo)
    val bookViewModel: BookViewModel = viewModel(factory = factory)

    var searchQuery by remember { mutableStateOf("") }

    val books by bookViewModel.dashboardBooks.observeAsState(initial = emptyList())

    // Fetch all books initially
    LaunchedEffect(Unit) {
        bookViewModel.getAllProduct()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Discover Books",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { query ->
                searchQuery = query
                bookViewModel.filterByTitle(query)
            },
            label = { Text("Search by book name...") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            // adds the "X" inside the search bar to clear search
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = {
                        searchQuery = ""
                        bookViewModel.clearFilters()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear Search"
                        )
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // FEATURE BUTTONS SECTION
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterCard(title = "Fiction", color = Color(0xFFE3F2FD), modifier = Modifier.weight(1f)) {
                bookViewModel.filterByGenre("Fiction")
            }
            FilterCard(title = "Fantasy", color = Color(0xFFF3E5F5), modifier = Modifier.weight(1f)) {
                bookViewModel.filterByGenre("Fantasy")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterCard(title = "Comedy", color = Color(0xFFE8F5E9), modifier = Modifier.weight(1f)) {
                bookViewModel.filterByGenre("Comedy")
            }
            FilterCard(title = "View All", color = Color(0xFFFFF3E0), modifier = Modifier.weight(1f)) {
                bookViewModel.clearFilters()
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- BOOK LIST ---
        if (books.isEmpty()) {
            // FIX: Changed 'fillWeight' to 'weight'
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("No books found for this selection")
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(books) { book ->
                    BookItem(book, onClick = { navController.navigate("detail/${book.bookId}") })
                }
            }
        }
    }
}
@Composable
fun FilterCard(
    title: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = color),
        modifier = modifier
            .height(80.dp)
            .padding(4.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun BookItem(book: BookModel, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = book.imageUrl,
                contentDescription = book.bookName,
                modifier = Modifier
                    .size(60.dp)
                    .clip(MaterialTheme.shapes.small),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Text(book.bookName, style = MaterialTheme.typography.titleMedium)
                Text(book.author, style = MaterialTheme.typography.bodySmall, color = Color.Gray)

                Text(book.genreId, style = MaterialTheme.typography.labelSmall, color = Color.Blue)
            }
        }
    }
}