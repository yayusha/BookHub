package com.example.sem3project.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.sem3project.model.BookModel
import com.example.sem3project.repo.BookRepoImpl
import com.example.sem3project.ui.theme.green20
import com.example.sem3project.viewmodel.BookViewModel
import com.example.sem3project.viewmodel.ReviewViewModel
import kotlinx.coroutines.launch

// Screen enum to handle panel navigation
enum class AdminScreen { BOOK_LIST, REPORTED_REVIEWS, SETTINGS }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookHomeScreen() {
    val bookViewModel: BookViewModel = remember { BookViewModel(BookRepoImpl()) }
    val reviewViewModel: ReviewViewModel = viewModel()
    val bookList by bookViewModel.allBooks.observeAsState(initial = emptyList())

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Navigation and Theme States
    var currentScreen by remember { mutableStateOf(AdminScreen.BOOK_LIST) }
    var isDarkTheme by remember { mutableStateOf(false) }
    var editingBook by remember { mutableStateOf<BookModel?>(null) }

    LaunchedEffect(Unit) { bookViewModel.getAllProduct() }

    // --- DARK MODE WRAPPER ---
    MaterialTheme(
        colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme()
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ModalDrawerSheet(
                        drawerContainerColor = MaterialTheme.colorScheme.surface
                    ) {
                        DrawerContent(
                            currentScreen = currentScreen,
                            onNavigate = { screen ->
                                currentScreen = screen
                                editingBook = null
                                scope.launch { drawerState.close() }
                            },
                            onLogout = {
                                com.google.firebase.auth.FirebaseAuth.getInstance().signOut()
                                Toast.makeText(context, "Logged Out Successfully", Toast.LENGTH_SHORT).show()
                                val intent = android.content.Intent(context, LoginActivity::class.java)
                                intent.flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK or android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
                                context.startActivity(intent)
                            }
                        )
                    }
                }
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    if (editingBook != null) {
                        EditBookView(
                            book = editingBook!!,
                            onSave = { updated ->
                                bookViewModel.updateBook(updated) { success, msg ->
                                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                    if (success) editingBook = null
                                }
                            },
                            onCancel = { editingBook = null }
                        )
                    } else {
                        when (currentScreen) {
                            AdminScreen.BOOK_LIST -> BookListScreen(
                                bookList = bookList,
                                bookViewModel = bookViewModel,
                                onEditClick = { editingBook = it },
                                onMenuClick = { scope.launch { drawerState.open() } }
                            )
                            AdminScreen.REPORTED_REVIEWS -> {
                                // Calling your separate working ReviewScreen file
                                ReviewScreen(
                                    viewModel = reviewViewModel,
                                    onMenuClick = { scope.launch { drawerState.open() } }
                                )
                            }
                            AdminScreen.SETTINGS -> SettingsScreen(
                                isDark = isDarkTheme,
                                onToggle = { isDarkTheme = it },
                                onMenu = { scope.launch { drawerState.open() } }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookListScreen(
    bookList: List<BookModel>?,
    bookViewModel: BookViewModel,
    onEditClick: (BookModel) -> Unit,
    onMenuClick: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var bookToDelete by remember { mutableStateOf<BookModel?>(null) }
    val context = LocalContext.current

    val filteredList = remember(bookList, searchQuery) {
        bookList?.filter { it.bookName.contains(searchQuery, ignoreCase = true) } ?: emptyList()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (isSearchActive) {
                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search books...") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                focusedTextColor = Color.White
                            )
                        )
                    } else { Text("Admin Dashboard") }
                },
                navigationIcon = { IconButton(onClick = onMenuClick) { Icon(Icons.Default.Menu, null) } },
                actions = {
                    IconButton(onClick = { isSearchActive = !isSearchActive; if (!isSearchActive) searchQuery = "" }) {
                        Icon(if (isSearchActive) Icons.Default.Close else Icons.Default.Search, null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = green20,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val intent = android.content.Intent(context, AddBook::class.java)
                    context.startActivity(intent)
                },
                containerColor = green20,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Book")
            }
        }
    ) { p ->
        Box(modifier = Modifier.padding(p)) {
            LazyColumn(Modifier.fillMaxSize().padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(filteredList) { book ->
                    BookCardUI(
                        book = book,
                        onEditClick = { onEditClick(book) },
                        onDeleteClick = { bookToDelete = book; showDeleteDialog = true }
                    )
                }
            }

            if (showDeleteDialog) {
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    title = { Text("Confirm Deletion") },
                    text = { Text("Delete '${bookToDelete?.bookName}' permanently?") },
                    confirmButton = {
                        Button(
                            onClick = {
                                bookViewModel.deleteBook(bookToDelete?.bookId ?: "") { success, msg ->
                                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                    showDeleteDialog = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) { Text("Delete") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel") }
                    }
                )
            }
        }
    }
}

@Composable
fun BookCardUI(book: BookModel, onEditClick: () -> Unit, onDeleteClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = green20),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
            ) {
                AsyncImage(
                    model = book.imageUrl,
                    contentDescription = null,
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = book.bookName,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 2,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
                Text(
                    text = "By : ${book.author}",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // 3. Buttons Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onEditClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.9f)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.width(80.dp).height(36.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("Edit", color = Color.Black, fontSize = 12.sp)
                }

                Button(
                    onClick = onDeleteClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.9f)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.width(80.dp).height(36.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("Delete", color = Color.Black, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun EditBookView(book: BookModel, onSave: (BookModel) -> Unit, onCancel: () -> Unit) {
    var name by remember { mutableStateOf(book.bookName) }
    var author by remember { mutableStateOf(book.author) }

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text("Edit Book Details", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = green20)
        Spacer(Modifier.height(20.dp))
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Book Title") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(value = author, onValueChange = { author = it }, label = { Text("Author") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(32.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = onCancel, modifier = Modifier.weight(1f)) { Text("Cancel") }
            Button(onClick = { onSave(book.copy(bookName = name, author = author)) }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = green20)) { Text("Save") }
        }
    }
}

@Composable
fun DrawerContent(
    currentScreen: AdminScreen,
    onNavigate: (AdminScreen) -> Unit,
    onLogout: () -> Unit
) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Admin Panel", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = green20)
        Spacer(Modifier.height(24.dp))

        NavigationDrawerItem(
            label = { Text("Manage Books") },
            selected = currentScreen == AdminScreen.BOOK_LIST,
            onClick = { onNavigate(AdminScreen.BOOK_LIST) },
            // "List" is a core icon that never fails
            icon = { Icon(Icons.Default.List, null) }
        )
        NavigationDrawerItem(
            label = { Text("View Reviews") },
            selected = currentScreen == AdminScreen.REPORTED_REVIEWS,
            onClick = { onNavigate(AdminScreen.REPORTED_REVIEWS) },
            // "Info" is a core icon
            icon = { Icon(Icons.Default.Info, null) }
        )
        NavigationDrawerItem(
            label = { Text("Settings") },
            selected = currentScreen == AdminScreen.SETTINGS,
            onClick = { onNavigate(AdminScreen.SETTINGS) },
            // "Settings" is a core icon
            icon = { Icon(Icons.Default.Settings, null) }
        )

        Spacer(modifier = Modifier.weight(1f))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(8.dp))

        NavigationDrawerItem(
            label = { Text("Logout", color = Color.Red) },
            selected = false,
            onClick = onLogout,
            // "ExitToApp" might be missing in basic, so we use "AccountCircle" or "Close"
            icon = { Icon(Icons.Default.Close, null, tint = Color.Red) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(isDark: Boolean, onToggle: (Boolean) -> Unit, onMenu: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = { IconButton(onClick = onMenu) { Icon(Icons.Default.Menu, null) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = green20, titleContentColor = Color.White)
            )
        }
    ) { p ->
        Column(Modifier.padding(p).padding(16.dp).fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Enable Dark Mode", Modifier.weight(1f), fontSize = 18.sp)
                Switch(checked = isDark, onCheckedChange = onToggle)
            }
        }
    }
}