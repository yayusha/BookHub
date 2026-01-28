@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardBody() {
    val context = LocalContext.current
    val activity = context as? Activity

    data class NavItem(val label: String, val icon: Int)

    var selectedIndex by remember { mutableStateOf(0) }
    var showSearch by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    val listItems = listOf(
        NavItem("Home", R.drawable.baseline_home_24),
        NavItem("Notification", R.drawable.baseline_notifications_24),
        NavItem("Profile", R.drawable.baseline_person_24)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = green20,
                    titleContentColor = White20,
                    navigationIconContentColor = White20,
                    actionIconContentColor = White20
                ),
                title = { Text("BookHub") },
                navigationIcon = {
                    IconButton(onClick = { /* Handle Drawer if needed */ }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_menu_24),
                            contentDescription = "Menu"
                        )
                    }
                },
                actions = {
                    // Added search toggle button so you can actually use the search bar!
                    IconButton(onClick = { showSearch = !showSearch }) {
                        Icon(
                            painter = painterResource(android.R.drawable.ic_menu_search),
                            contentDescription = "Search"
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
                        onClick = {
                            selectedIndex = index
                            if (index != 0) showSearch = false
                        },
                        icon = {
                            Icon(
                                painter = painterResource(item.icon),
                                contentDescription = item.label
                            )
                        },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { paddingValues ->
        // The combined logic:
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedIndex) {
                0 -> {
                    // Home logic with search bar support
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
                    HomeScreen() // Unified name
                }
                1 -> NotificationScreen() // Or UserScreen() depending on your preference
                2 -> ProfileScreen()
            }
        }
    }
}