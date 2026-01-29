package com.example.sem3project.view

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sem3project.ui.theme.White20
import com.example.sem3project.ui.theme.green20
import com.example.sem3project.R
import com.example.sem3project.ui.theme.Sem3ProjectTheme
import com.example.sem3project.utils.ThemePreferences

class UserDashboard : ComponentActivity() {
    private lateinit var themePreferences: ThemePreferences
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        themePreferences = ThemePreferences(this)
        
        enableEdgeToEdge()
        setContent {
            var isDarkMode by remember { mutableStateOf(themePreferences.isDarkMode()) }
            
            Sem3ProjectTheme(darkTheme = isDarkMode) {
                DashboardBody(
                    isDarkMode = isDarkMode,
                    onThemeChange = { newMode ->
                        isDarkMode = newMode
                        themePreferences.setDarkMode(newMode)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardBody(
    isDarkMode: Boolean = false,
    onThemeChange: (Boolean) -> Unit = {}
) {

    val context = LocalContext.current
    val activity = context as Activity

    val auth = com.google.firebase.auth.FirebaseAuth.getInstance()
    val currentUid = auth.currentUser?.uid ?: ""

    data class NavItem(val label: String, val icon: Int)

    var selectedIndex by remember { mutableStateOf(0) }
    var showSearch by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    val listItems = listOf(
        NavItem("Home", R.drawable.baseline_home_24),
        NavItem("Notification", R.drawable.baseline_notifications_24),
        NavItem("Profile", R.drawable.baseline_person_24)
    )

    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = green20,
                    titleContentColor = White20
                ),
                title = { Text("BookHub") },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_menu_24),
                            contentDescription = "Menu"
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                listItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index; showSearch = false },
                        icon = { Icon(painter = painterResource(item.icon), contentDescription = item.label) },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedIndex) {
                0 -> {
                    // NavHost inside Home tab
                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") {
                            Column {
                                if (showSearch) {
                                    OutlinedTextField(
                                        value = searchText,
                                        onValueChange = { searchText = it },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        label = { Text("Search books...") },
                                        singleLine = true
                                    )
                                }
                                HomeScreen(navController = navController)
                            }
                        }
                        composable("detail/{bookId}") { backStackEntry ->
                            val bookId = backStackEntry.arguments?.getString("bookId") ?: ""
                            BookDetailsScreen(bookId = bookId,
                                navController = navController,
                                currentUserId = currentUid
                            )
                        }
                    }
                }
                1 -> NotificationScreen(currentUserId = currentUid)
                2 -> ProfileScreen(
                    navController = navController,
                    isDarkMode = isDarkMode,
                    onThemeChange = onThemeChange
                )
            }
        }
    }
}
