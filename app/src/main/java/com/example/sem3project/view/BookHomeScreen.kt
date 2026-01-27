package com.example.sem3project.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.sem3project.R
import com.example.sem3project.model.BookModel
import com.example.sem3project.repo.BookRepoImpl
import com.example.sem3project.ui.theme.White20
import com.example.sem3project.ui.theme.green20
import com.example.sem3project.viewmodel.BookViewModel
import com.example.sem3project.viewmodel.ReviewViewModel
import kotlinx.coroutines.launch

// Navigation destinations enum
enum class AdminScreen {
    BOOK_LIST,
    REPORTED_REVIEWS,
    SETTINGS
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookHomeScreen() {
    val bookViewModel: BookViewModel = remember { BookViewModel(BookRepoImpl()) }
    val reviewViewModel: ReviewViewModel = viewModel()
    val bookList by bookViewModel.allBooks.observeAsState(initial = emptyList())
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // State to track current screen
    var currentScreen by remember { mutableStateOf(AdminScreen.BOOK_LIST) }

    LaunchedEffect(Unit) {
        bookViewModel.getAllProduct()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.White
            ) {
                DrawerContent(
                    currentScreen = currentScreen,
                    onNavigate = { screen ->
                        currentScreen = screen
                        scope.launch {
                            drawerState.close()
                        }
                    },
                    onLogout = {
                        // Handle logout
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )
            }
        }
    ) {
        // Show different screens based on currentScreen state
        when (currentScreen) {
            AdminScreen.BOOK_LIST -> {
                BookListScreen(
                    bookList = bookList,
                    bookViewModel = bookViewModel,
                    onMenuClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
            }
            AdminScreen.REPORTED_REVIEWS -> {
                ReviewScreen(
                    viewModel = reviewViewModel,
                    onMenuClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
            }
            AdminScreen.SETTINGS -> {
                SettingsScreen(
                    onMenuClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun DrawerContent(
    currentScreen: AdminScreen,
    onNavigate: (AdminScreen) -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 24.dp)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(bottom = 16.dp)
        ) {
            Column {
                Text(
                    text = "Book Hub",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = green20
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Admin Panel",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }

        Divider()

        Spacer(modifier = Modifier.height(16.dp))

        // Menu Items
        DrawerMenuItem(
            icon = Icons.Default.Home,
            label = "Dashboard",
            selected = currentScreen == AdminScreen.BOOK_LIST,
            onClick = { onNavigate(AdminScreen.BOOK_LIST) }
        )

        DrawerMenuItem(
            icon = Icons.Default.Star,
            label = "View reported reviews",
            selected = currentScreen == AdminScreen.REPORTED_REVIEWS,
            onClick = { onNavigate(AdminScreen.REPORTED_REVIEWS) }
        )

        DrawerMenuItem(
            icon = Icons.Default.Settings,
            label = "Settings",
            selected = currentScreen == AdminScreen.SETTINGS,
            onClick = { onNavigate(AdminScreen.SETTINGS) }
        )

        Spacer(modifier = Modifier.weight(1f))

        DrawerMenuItem(
            icon = Icons.Default.Lock,
            label = "Logout",
            selected = false,
            onClick = onLogout
        )
    }
}

@Composable
fun DrawerMenuItem(
    icon: ImageVector,
    label: String,
    selected: Boolean = false,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (selected) Color.White else green20
            )
        },
        label = {
            Text(
                text = label,
                fontSize = 16.sp,
                color = if (selected) Color.White else Color.Black
            )
        },
        selected = selected,
        onClick = onClick,
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
        colors = NavigationDrawerItemDefaults.colors(
            unselectedContainerColor = Color.Transparent,
            selectedContainerColor = green20
        )
    )
}

// Separate composable for the book list screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookListScreen(
    bookList: List<BookModel>?,
    bookViewModel: BookViewModel,
    onMenuClick: () -> Unit
) {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(green20)
                    .padding(top = 32.dp, bottom = 12.dp, start = 8.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onMenuClick) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
                Text("Admin Dashboard", color = Color.White, fontWeight = FontWeight.Bold)

                IconButton(onClick = { /* TODO: Search */ }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO: Navigate to Add Book */ },
                containerColor = Color(0xFFEADDFF),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = Color.Black
                )
            }
        }
    ) { innerPadding ->
        val displayList = bookList ?: emptyList()

        if (displayList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = green20)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(12.dp)
            ) {
                items(displayList) { item ->
                    BookCardUI(book = item, viewModel = bookViewModel)
                }
            }
        }
    }
}

// Placeholder Settings Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onMenuClick: () -> Unit
) {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(green20)
                    .padding(top = 32.dp, bottom = 12.dp, start = 8.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onMenuClick) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
                Text("Settings", color = Color.White, fontWeight = FontWeight.Bold)

                // Empty space to balance the layout
                Spacer(modifier = Modifier.size(48.dp))
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.headlineMedium,
                    color = green20,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Settings content will be added here",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun BookCardUI(book: BookModel, viewModel: BookViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        colors = CardDefaults.cardColors(containerColor = green20),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.DarkGray),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(book.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Book Image",
                    modifier = Modifier.fillMaxSize(),
                    placeholder = painterResource(R.drawable.placeholder),
                    error = painterResource(R.drawable.placeholder)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = book.bookName,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "By : ${book.author}",
                    color = Color.LightGray,
                    fontSize = 14.sp
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { /* TODO: Navigate to Edit */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text("Edit", color = Color.Black, fontSize = 12.sp)
                }

                Button(
                    onClick = {
                        viewModel.deleteBook(book.bookId) { success, message ->
                            /* Handle result */
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text("Delete", color = Color.Black, fontSize = 12.sp)
                }
            }
        }
    }
}