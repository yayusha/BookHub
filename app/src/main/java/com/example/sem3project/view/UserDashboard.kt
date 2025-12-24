package com.example.sem3project.view


import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.sem3project.R
import com.example.sem3project.ui.theme.White20
import com.example.sem3project.ui.theme.green20

class UserDashboard : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DashboardBody()
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardBody(){
    val context = LocalContext.current
    val activity = context as Activity


    data class NavItem(val label: String,val icon: Int)
    var selectedIndex by remember { mutableStateOf(0) }

    val listItems = listOf(
        NavItem(label = "Home", R.drawable.baseline_home_24),
        NavItem(label = "Notification", R.drawable.baseline_notifications_24),
        NavItem(label = "Profile", R.drawable.baseline_person_24),

        )
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    navigationIconContentColor = White20,
                    actionIconContentColor = White20,
                    titleContentColor = White20,
                    containerColor = green20
                ),
                title = {Text("BookHub")},
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_menu_24),
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_search_24),
                            contentDescription = null
                        )
                    }

                }
            )
        },
        bottomBar = {
            NavigationBar {
                listItems.forEachIndexed { index, item->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                painter = painterResource(item.icon),
                                contentDescription = null
                            )
                        },
                        label = {
                            Text(item.label)
                        },
                        onClick = {
                            selectedIndex = index
                        },
                        selected = selectedIndex == index
                    )
                }
            }
        }

    ) {padding->
        Column (
            modifier = Modifier.fillMaxSize().padding(padding)
        ){
            when(selectedIndex){
                0 -> HomeScreen()
                1 -> NotificationScreen()
                2 -> ProfileScreen()
                else -> HomeScreen()
            }
        }
    }
}