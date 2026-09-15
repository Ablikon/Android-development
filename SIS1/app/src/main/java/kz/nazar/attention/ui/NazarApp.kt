package kz.nazar.attention.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kz.nazar.attention.navigation.Destination
import kz.nazar.attention.ui.screens.ChainsScreen
import kz.nazar.attention.ui.screens.SettingsScreen
import kz.nazar.attention.ui.screens.TodayScreen

@ExperimentalMaterial3Api
@Composable
fun NazarApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: Destination.Today.route

    Scaffold(
        topBar = { TopAppBar(title = { Text(text = "Nazar") }) },
        bottomBar = {
            NavigationBar {
                Destination.entries.forEach { destination ->
                    NavigationBarItem(
                        selected = currentRoute == destination.route,
                        onClick = {
                            if (currentRoute != destination.route) {
                                navController.navigate(destination.route) {
                                    popUpTo(Destination.Today.route)
                                    launchSingleTop = true
                                }
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = destination.icon,
                                contentDescription = destination.label,
                            )
                        },
                        label = { Text(text = destination.label) },
                    )
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Destination.Today.route,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(Destination.Today.route) { TodayScreen() }
            composable(Destination.Chains.route) { ChainsScreen() }
            composable(Destination.Settings.route) { SettingsScreen() }
        }
    }
}
