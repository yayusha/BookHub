package com.example.sem3project.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items // Added for cleaner list handling
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sem3project.R
import com.example.sem3project.model.BookModel

@Composable
fun Homescreen() {
    var searchQuery by remember { mutableStateOf("") }
    var selectedGenre by remember { mutableStateOf("All") }
    val genres = listOf("All", "Fiction", "Romance", "Mystery", "Self-help", "Finance")

    val allBooks = listOf(
        // Existing
        BookModel("1", "The Alchemist", "Paulo Coelho", "Fiction", "A story about destiny", "book1"),
        BookModel("2", "Atomic Habits", "James Clear", "Self-help", "Guide to building habits", "book2"),
        BookModel("3", "Rich Dad Poor Dad", "Robert T. Kiyosaki", "Finance", "Personal finance lessons", "book3"),
        BookModel("4", "Think and Grow Rich", "Napoleon Hill", "Mystery", "Success principles", "book4"),

        // Fiction
        BookModel("6", "1984", "George Orwell", "Fiction", "Dystopian novel about surveillance and freedom", "book6"),
        BookModel("7", "The Great Gatsby", "F. Scott Fitzgerald", "Fiction", "Jazz Age story of ambition and love", "book7"),

        // Mystery
        BookModel("8", "The Murder of Roger Ackroyd", "Agatha Christie", "Mystery", "Detective classic with a famous twist", "book8"),
        BookModel("10", "Gone Girl", "Gillian Flynn", "Mystery", "Psychological thriller with shocking twists", "book10"),

        // Romance
        BookModel("11", "Pride and Prejudice", "Jane Austen", "Romance", "Beloved Regency romance full of wit", "book11"),
        BookModel("12", "Outlander", "Diana Gabaldon", "Romance", "Historical romance with time-travel twist", "book12"),
        BookModel("13", "The Notebook", "Nicholas Sparks", "Romance", "Contemporary tear-jerker romance", "book13"),

        // Self-help
        BookModel("14", "The 7 Habits of Highly Effective People", "Stephen R. Covey", "Self-help", "Foundational personal growth classic", "book14"),
        BookModel("16", "The Power of Now", "Eckhart Tolle", "Self-help", "Spiritual self-help on mindfulness and presence", "book16")
    )

    // Filter books by genre + search
    val filteredBooks = allBooks.filter {
        (selectedGenre == "All" || it.genreId == selectedGenre) &&
                (searchQuery.isBlank() || it.bookName.contains(searchQuery, ignoreCase = true) || it.author.contains(searchQuery, ignoreCase = true))
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Home",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search books...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(genres.size) { index ->
                    val genre = genres[index]
                    FilterChip(
                        selected = selectedGenre == genre,
                        onClick = { selectedGenre = genre },
                        label = { Text(genre) }
                    )
                }
            }
        }

        // Section Header
        item {
            Text(
                text = "$selectedGenre Books",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        //Using items directly inside the main LazyColumn to avoid nesting crash
        items(filteredBooks) { book ->
            BookItem(
                cover = when (book.imageUrl) {
                    "book1" -> R.drawable.book1
                    "book2" -> R.drawable.book2
                    "book3" -> R.drawable.book3
                    "book4" -> R.drawable.book4
                    "book6" -> R.drawable.book6
                    "book7" -> R.drawable.book7
                    "book10" -> R.drawable.book10
                    "book11" -> R.drawable.book11
                    "book12" -> R.drawable.book12
                    "book14" -> R.drawable.book14
                    "book16" -> R.drawable.book16
                    else -> R.drawable.book1
                },
                title = book.bookName,
                author = book.author
            )
        }
    }
}

@Composable
fun BookItem(
    cover: Int,
    title: String,
    author: String
) {
    // Card wrapper for styling
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            Image(
                painter = painterResource(id = cover),
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(width = 55.dp, height = 75.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(text = title, fontWeight = FontWeight.Medium)
                Text(
                    text = author,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}